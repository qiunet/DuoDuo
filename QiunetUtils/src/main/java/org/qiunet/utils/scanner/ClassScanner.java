package org.qiunet.utils.scanner;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.timer.TimerManager;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiunet
 *         Created on 17/1/23 18:22.
 */
public final class ClassScanner implements IApplicationContext {
	private static final Scanner [] scanners = new Scanner[]{
			Scanners.FieldsAnnotated,
			Scanners.MethodsAnnotated,
			Scanners.TypesAnnotated,
			Scanners.SubTypes,
	};

	private final ConcurrentHashMap<Class, Object> beanInstances = new ConcurrentHashMap<>();
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	/**
	 * 存储一些参数
	 */
	private final ArgsContainer argsContainer = new ArgsContainer();
	private static ClassScanner instance;
	/**
	 * 扫描工具类
	 */
	private final Reflections reflections;
	/**
	 * 可以扫描的类型
	 */
	private final int scannerTypes;
	/**
	 * 已经回收. 不能再取数据.
	 */
	private boolean recycled;

	private ClassScanner(ScannerType... scannerTypes) {
		if (instance != null) {
			throw new CustomException("Instance Duplication!");
		}
		this.reflections = new Reflections("org.qiunet", scanners);
		int scannerType = 0;
		for (ScannerType type : scannerTypes) {
			scannerType |= type.getStatus();
		}
		this.scannerTypes = scannerType;
		instance = this;
	}

	public static ClassScanner getInstance(ScannerType... scannerType) {
		if (instance == null) {
			synchronized (ClassScanner.class) {
				if (instance == null)
				{
					new ClassScanner(scannerType);
				}
			}
		}
		return instance;
	}

	private final AtomicBoolean scannered = new AtomicBoolean();
	public void scanner(String ... packetPrefix) {
		try {
			this.scanner0(packetPrefix);
		} catch (Exception e) {
			logger.error("Scanner Exception:", e);
			System.exit(1);
		}finally {
			// 后面不需要了. 也不允许业务来读取. 释放内存
			reflections.getStore().clear();
			recycled = true;
		}
	}
	private Set<String> scannerClassNames;
	private void scanner0(String ... packetPrefix) throws Exception {
		if (scannered.get() || ! scannered.compareAndSet(false, true)) {
			logger.warn("ClassScanner was initialization , ignore this!");
			return;
		}

		if (packetPrefix != null && packetPrefix.length > 0) {
			this.reflections.merge(new Reflections(packetPrefix, scanners));
		}

		Set<Class<? extends IApplicationContextAware>> subTypesOf = this.reflections.getSubTypesOf(IApplicationContextAware.class);
		List<IApplicationContextAware> collect = subTypesOf.stream()
			.map(aClass -> (IApplicationContextAware) getInstanceOfClass(aClass))
			.sorted((o1, o2) -> ComparisonChain.start().compare(o2.order(), o1.order()).compare(o1.scannerType().ordinal(), o2.scannerType().ordinal()).result())
			.collect(Collectors.toList());

		AtomicReference<Exception> reference = new AtomicReference<>();
		CountDownLatch latch = new CountDownLatch(collect.size());
		Set<DFuture> futures = Sets.newHashSet();

		if (logger.isDebugEnabled()) {
			scannerClassNames = collect.stream().map(o -> o.getClass().getSimpleName()).collect(Collectors.toSet());
			logger.debug("scanner start count: {}, detail: {}", collect.size(),
					Arrays.toString(scannerClassNames.toArray()));
		}
		for (IApplicationContextAware instance : collect) {
			logger.debug("scanner start detail: {}", instance.getClass().getName());
			// 不相同. 并且都不是ALL
			if (! this.isPrepare(instance.scannerType())) {
				this.countdown(latch, instance.getClass().getSimpleName());
				continue;
			}

			if (instance.order() > 0) {
				this.run(instance);
				this.countdown(latch, instance.getClass().getSimpleName());
				continue;
			}

			DFuture<String> future = TimerManager.executorNow(() -> {
				run(instance);
				return instance.getClass().getSimpleName();
			});

			futures.add(future);

			future.whenComplete((res, ex) -> {
				if (ex != null) {
					logger.error("==scanner exception!==");
					reference.compareAndSet(null, (Exception) ex);
					futures.forEach(future0 -> future0.cancel(true));
					long count = latch.getCount();
					for (long i = 0; i < count; i++) {
						this.countdown(latch, null);
					}
				}else {
					this.countdown(latch, res);
				}
			});
		}

		latch.await();
		if (reference.get() != null) {
			throw reference.get();
		}
	}

	private void countdown(CountDownLatch latch, String className) {
		latch.countDown();
		if (logger.isDebugEnabled() && className != null) {
			scannerClassNames.remove(className);
			logger.debug("countdown  {}, last scanner class {}", className, Arrays.toString(scannerClassNames.toArray()));
		}
	}

	/**
	 * 添加一些参数给applicationContext
	 * @param argKey
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public <T> ClassScanner addParam(ArgumentKey<T> argKey, T obj) {
		this.argsContainer.setVal(argKey, obj);
		return this;
	}

	private void run(IApplicationContextAware applicationContextAware) throws Exception {
		long millisSeconds = DateUtil.calConsumeMillisSeconds(() -> {
			applicationContextAware.setApplicationContext(this, argsContainer);
		});
		if (logger.isDebugEnabled()) {
			logger.debug("ApplicationContext [{}] consume [{}] ms", applicationContextAware.getClass().getSimpleName(), millisSeconds);
		}
	}

	@Override
	public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
		return reflections.getSubTypesOf(type);
	}

	@Override
	public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
		if (recycled) {
			throw new RuntimeException("Already recycled!");
		}
		return reflections.getTypesAnnotatedWith(annotation);
	}

	@Override
	public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
		if (recycled) {
			throw new RuntimeException("Already recycled!");
		}
		return reflections.getFieldsAnnotatedWith(annotation);
	}

	@Override
	public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		if (recycled) {
			throw new RuntimeException("Already recycled!");
		}
		return reflections.getMethodsAnnotatedWith(annotation);
	}

	@Override
	public boolean isPrepare(ScannerType scannerType) {
		return scannerType.test(this.scannerTypes);
	}

	@Override
	public Object getInstanceOfClass(Class clazz, Object... params) {
		return beanInstances.computeIfAbsent(clazz, key -> {
			Optional<Object> first = Stream.of(key.getDeclaredFields())
				.filter(f -> Modifier.isStatic(f.getModifiers()))
				.filter(f -> f.getType() == key)
				.map(f -> ReflectUtil.getField(f, (Object) null))
				.filter(Objects::nonNull)
				.findFirst();

			if (first.isPresent()) {
				return first.get();
			}

			Object ret = ReflectUtil.newInstance(key, params);
			if (ret != null) {
				return ret;
			}

			throw new NullPointerException("can not get instance for class ["+key.getName()+"]");
		});
	}
}
