package org.qiunet.utils.classScanner;

import org.qiunet.utils.logger.LoggerType;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.scanners.Scanner;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author qiunet
 *         Created on 17/1/23 18:22.
 */
public final class ClassScanner implements IApplicationContext {
	private static final Scanner [] scanners = new Scanner[]{new MethodAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner(), new TypeAnnotationsScanner()};
	private Logger logger = LoggerType.DUODUO.getLogger();
	private Reflections reflections;

	private volatile static ClassScanner instance;

	private ClassScanner() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		this.reflections = new Reflections("org.qiunet", scanners);
		instance = this;
	}

	public static ClassScanner getInstance() {
		if (instance == null) {
			synchronized (ClassScanner.class) {
				if (instance == null)
				{
					new ClassScanner();
				}
			}
		}
		return instance;
	}

	public void scanner(String ... packetPrefix){
		if (packetPrefix != null && packetPrefix.length > 0) {
			this.reflections.merge(new Reflections(packetPrefix, scanners));
		}
		Set<Class<? extends IApplicationContextAware>> subTypesOf = this.reflections.getSubTypesOf(IApplicationContextAware.class);
		for (Class<? extends IApplicationContextAware> aClass : subTypesOf) {
			try {
				Constructor<? extends IApplicationContextAware> constructor = aClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance().setApplicationContext(this);
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
				e.printStackTrace();
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
		Optional<Field> first = Stream.of(clazz.getDeclaredFields())
			.filter(f -> Modifier.isStatic(f.getModifiers()))
			.filter(f -> f.getType() == clazz)
			.findFirst();

		if (first.isPresent()) {
			Field field = first.get();
			field.setAccessible(true);
			try {
				return field.get(null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}else {
			Class<?> [] clazzes = new Class[params.length];
			for (int i = 0; i < params.length; i++) {
				clazzes[i] = params[i].getClass();
			}
			try {
				Constructor constructor = clazz.getDeclaredConstructor(clazzes);
				constructor.setAccessible(true);
				return constructor.newInstance(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new NullPointerException("can not get instance for class ["+clazz.getName()+"]");
	}
}
