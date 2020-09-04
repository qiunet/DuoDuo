package org.qiunet.scanner;

import com.google.common.collect.ComparisonChain;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
	private volatile static ClassScanner instance;

	private ClassScanner(ScannerType scannerType) {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
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
	public void scanner(String ... packetPrefix) throws Exception {
		if (scannered.get()) {
			logger.warn("ClassScanner was initialization , ignore this!");
			return;
		}

		if (scannered.compareAndSet(false, true)) {
			if (packetPrefix != null && packetPrefix.length > 0) {
				this.reflections.merge(new Reflections(packetPrefix, scanners));
			}
			Set<Class<? extends IApplicationContextAware>> subTypesOf = this.reflections.getSubTypesOf(IApplicationContextAware.class);
			List<IApplicationContextAware> collect = subTypesOf.stream()
				.map(aClass -> (IApplicationContextAware) getInstanceOfClass(aClass))
				.sorted((o1, o2) -> ComparisonChain.start().compare(o2.order(), o1.order()).result())
				.collect(Collectors.toList());

			for (IApplicationContextAware instance : collect) {
				// 不相同. 并且都不是ALL
				if (instance.scannerType() != this.scannerType
				&& instance.scannerType() != ScannerType.ALL
				&& this.scannerType != ScannerType.ALL) {
					continue;
				}

				try {
					instance.setApplicationContext(this);
				}catch (Exception e) {
					logger.error("===========Scanner Exception============:"+e.getMessage());
					throw e;
				}
			}
		}
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
		if (beanInstances.containsKey(clazz)) {
			return beanInstances.get(clazz);
		}

//		if (! Enum.class.isAssignableFrom(clazz)
//			&& !clazz.isAnnotationPresent(Singleton.class)) {
//			throw new RuntimeException("["+clazz.getName()+"] Must be Singleton And Set Singleton Annotation");
//		}


		Optional<Field> first = Stream.of(clazz.getDeclaredFields())
			.filter(f -> Modifier.isStatic(f.getModifiers()))
			.filter(f -> f.getType() == clazz)
			.findFirst();

		if (first.isPresent()) {
			Field field = first.get();
			field.setAccessible(true);
			try {
				Object ret = field.get(null);
				if (ret != null) {
					beanInstances.put(clazz, ret);
					return ret;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		Class<?> [] clazzes = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			clazzes[i] = params[i].getClass();
		}

		Constructor[] constructors = clazz.getDeclaredConstructors();
		for (Constructor constructor : constructors) {
			if (constructor.getParameterCount() != clazzes.length) {
				continue;
			}

			boolean allMatch = IntStream.range(0, clazzes.length).mapToObj(i -> clazzes[i] == constructor.getParameterTypes()[i]).allMatch(Boolean::booleanValue);
			if (! allMatch) {
				continue;
			}

			constructor.setAccessible(true);
			try {
				Object ret = constructor.newInstance(params);
				beanInstances.put(clazz, ret);
				return ret;
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		throw new NullPointerException("can not get instance for class ["+clazz.getName()+"]");
	}
}
