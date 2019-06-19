package org.qiunet.utils.classScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public interface IApplicationContext {
	/**
	 * gets all sub types in hierarchy of a given type
	 * <p/>depends on SubTypesScanner configured
	 */
	<T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type);
	/**
	 * get types annotated with a given annotation, both classes and annotations
	 * <p>{@link java.lang.annotation.Inherited} is not honored by default.
	 * <p>when honoring @Inherited, meta-annotation should only effect annotated super classes and its sub types
	 * <p><i>Note that this (@Inherited) meta-annotation type has no effect if the annotated type is used for anything other then a class.
	 * Also, this meta-annotation causes annotations to be inherited only from superclasses; annotations on implemented interfaces have no effect.</i>
	 * <p/>depends on TypeAnnotationsScanner and SubTypesScanner configured
	 */
	Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation);
	/**
	 * get all fields annotated with a given annotation
	 * <p/>depends on FieldAnnotationsScanner configured
	 */
	Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation);
	/** get parameter names of given {@code method}
	 * <p>depends on MethodParameterNamesScanner configured
	 */
	List<String> getMethodParamNames(Method method);
	/** get parameter names of given {@code constructor}
	 * <p>depends on MethodParameterNamesScanner configured
	 */
	List<String> getConstructorParamNames(Constructor constructor);
	/**
	 * get all methods annotated with a given annotation
	 * <p/>depends on MethodAnnotationsScanner configured
	 */
	Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation);
	/** get resources relative paths where simple name (key) matches given regular expression
	 * <p>depends on ResourcesScanner configured
	 * <pre>Set<String> xmls = reflections.getResources(".*\\.xml");</pre>
	 */
	Set<String> getResources(final Pattern pattern);
}
