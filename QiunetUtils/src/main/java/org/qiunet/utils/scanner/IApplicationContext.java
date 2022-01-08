package org.qiunet.utils.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

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
	/**
	 * get all methods annotated with a given annotation
	 * <p/>depends on MethodAnnotationsScanner configured
	 */
	Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation);
	/***
	 * 返回class的对象
	 * 可以是自身持有对象的单例 没有就new 一个.
	 * @param clazz
	 * @return
	 */
	Object getInstanceOfClass(Class clazz, Object... params);

	/**
	 * 是否有扫描 scannerType
	 * @param scannerType
	 * @return
	 */
	boolean isPrepare(ScannerType scannerType);
}
