package org.qiunet.utils.classScanner;

import org.qiunet.utils.logger.LoggerType;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author qiunet
 *         Created on 17/1/23 18:22.
 */
public final class ClassScanner implements IApplicationContext {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private Reflections reflections;

	private volatile static ClassScanner instance;

	private ClassScanner() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		this.reflections = new Reflections("org.qiunet");
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
			this.reflections.merge(new Reflections(packetPrefix));
		}
		Set<Class<? extends IApplicationContextAware>> subTypesOf = this.reflections.getSubTypesOf(IApplicationContextAware.class);
		for (Class<? extends IApplicationContextAware> aClass : subTypesOf) {
			try {
				aClass.newInstance().setApplicationContext(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
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
	public List<String> getMethodParamNames(Method method) {
		return reflections.getMethodParamNames(method);
	}

	@Override
	public List<String> getConstructorParamNames(Constructor constructor) {
		return reflections.getConstructorParamNames(constructor);
	}

	@Override
	public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		return reflections.getMethodsAnnotatedWith(annotation);
	}

	@Override
	public Set<String> getResources(Pattern pattern) {
		return reflections.getResources(pattern);
	}
}
