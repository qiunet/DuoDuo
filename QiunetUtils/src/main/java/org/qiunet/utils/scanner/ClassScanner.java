package org.qiunet.utils.scanner;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.timer.TimerManager;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
	private static final Scanner [] scanners = new Scanner[]{new MethodAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner(), new TypeAnnotationsScanner()};
	private ConcurrentHashMap<Class, Object> beanInstances = new ConcurrentHashMap<>();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Reflections reflections;
	private ScannerType scannerType;
	/**
	 * 存储一些参数
	 */
	private ArgsContainer argsContainer = new ArgsContainer();

	private static ClassScanner instance;

	private ClassScanner(ScannerType scannerType) {
		if (instance != null) {
			throw new CustomException("Instance Duplication!");
		}
		this.reflections = new Reflections("org.qiunet", scanners);
		this.scannerType = scannerType;
		instance = this;
	}

	public static ClassScanner getInstance(ScannerType scannerType) {
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

	public static ClassScanner getInstance() {
		return getInstance(ScannerType.ALL);
	}

	private AtomicBoolean scannered = new AtomicBoolean();
	public void scanner(String ... packetPrefix) {
		try {
			this.scanner0(packetPrefix);
		} catch (Exception e) {
			logger.error("Scanner Exception:", e);
			System.exit(1);
		}
	}
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
			.sorted((o1, o2) -> ComparisonChain.start().compare(o2.order(), o1.order()).result())
			.collect(Collectors.toList());

		AtomicReference<Exception> reference = new AtomicReference<>();
		CountDownLatch latch = new CountDownLatch(subTypesOf.size());
		Set<DFuture> futures = Sets.newHashSet();

		for (IApplicationContextAware instance : collect) {
			// 不相同. 并且都不是ALL
			if (instance.scannerType() != this.scannerType
			&& instance.scannerType() != ScannerType.ALL
			&& this.scannerType != ScannerType.ALL) {
				latch.countDown();
				continue;
			}

			if (instance.order() > 0) {
				this.run(instance);
				latch.countDown();
				continue;
			}

			DFuture<Boolean> future = TimerManager.executor.executorNow(() -> {
				run(instance);
				return true;
			});

			futures.add(future);

			future.whenComplete((res, ex) -> {
				latch.countDown();
				if (ex != null) {
					reference.compareAndSet(null, (Exception) ex);
					futures.forEach(future0 -> future0.cancel(true));
					for (long i = 0; i < latch.getCount(); i++) {
						latch.countDown();
					}
				}
			});
		}

		latch.await();
		if (reference.get() != null) {
			throw reference.get();
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
		applicationContextAware.setApplicationContext(this, argsContainer);
	}

	@Override
	public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
		return reflections.getSubTypesOf(type);
	}

	@Override
	public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getTypesAnnotatedWith(annotation);
	}

	@Override
	public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getFieldsAnnotatedWith(annotation);
	}

	@Override
	public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getMethodsAnnotatedWith(annotation);
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
