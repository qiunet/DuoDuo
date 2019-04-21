package org.qiunet.utils.classScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public interface IApplicationContext {
	/**
	 *
	 * @param annotation
	 * @return
	 */
	Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation);

	/***
	 *
	 * @param annotation
	 * @return
	 */
	Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation);

	/***
	 *
	 * @param method
	 * @return
	 */
	List<String> getMethodParamNames(Method method);

	/***
	 *
	 * @param constructor
	 * @return
	 */
	List<String> getConstructorParamNames(Constructor constructor);
}
