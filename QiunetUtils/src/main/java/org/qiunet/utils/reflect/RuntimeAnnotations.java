package org.qiunet.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * 运行时注解添加. 一般情况少用.
 *
 * @author qiunet
 * 2022/4/19 15:00
 */
public final class RuntimeAnnotations {
	private static final Constructor<?> AnnotationData_constructor;
	private static final Method Class_annotationData;
	private static final Field Class_classRedefinedCount;
	private static final Field AnnotationData_annotations;
	private static final Field class_declaredAnotations;
	private static final Field field_declaredAnotations;
	private static final Method Atomic_casAnnotationData;
	private static final Class<?> Atomic_class;

	static{
		try {

			Atomic_class = Class.forName("java.lang.Class$Atomic");
			Class<?> class_AnnotationData = Class.forName("java.lang.Class$AnnotationData");

			AnnotationData_constructor = class_AnnotationData.getDeclaredConstructor(Map.class, Map.class, int.class);
			AnnotationData_constructor.setAccessible(true);
			Class_annotationData = Class.class.getDeclaredMethod("annotationData");
			Class_annotationData.setAccessible(true);

			Class_classRedefinedCount= Class.class.getDeclaredField("classRedefinedCount");
			Class_classRedefinedCount.setAccessible(true);

			AnnotationData_annotations = class_AnnotationData.getDeclaredField("annotations");
			AnnotationData_annotations.setAccessible(true);
			class_declaredAnotations = class_AnnotationData.getDeclaredField("declaredAnnotations");
			class_declaredAnotations.setAccessible(true);

			field_declaredAnotations = Field.class.getDeclaredField("declaredAnnotations");
			field_declaredAnotations.setAccessible(true);

			Atomic_casAnnotationData = Atomic_class.getDeclaredMethod("casAnnotationData", Class.class, class_AnnotationData, class_AnnotationData);
			Atomic_casAnnotationData.setAccessible(true);

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 给field添加注解
	 *
	 * @param field
	 * @param annotation
	 * @param <T>
	 */
	public static <T extends Annotation> void putAnnotation(Field field, T annotation){
		try {
			Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) field_declaredAnotations.get(field);
			Map<Class<? extends Annotation>, Annotation> temp = new HashMap<>();
			if (map != null) {
				temp.putAll(map);
			}
			temp.put(annotation.annotationType(), annotation);
			field_declaredAnotations.set(field, temp);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass, T annotation){
		try {
			while (true) { // retry loop
				int classRedefinedCount = Class_classRedefinedCount.getInt(c);
				Object /*AnnotationData*/ annotationData = Class_annotationData.invoke(c);
				// null or stale annotationData -> optimistically create new instance
				Object newAnnotationData = createAnnotationData(annotationData, annotationClass, annotation, classRedefinedCount);
				// try to install it
				if ((boolean) Atomic_casAnnotationData.invoke(Atomic_class, c, annotationData, newAnnotationData)) {
					// successfully installed new AnnotationData
					break;
				}
			}
		} catch(Exception e){
			throw new IllegalStateException(e);
		}

	}

	private static <T extends Annotation> Object createAnnotationData(Object annotationData, Class<T> annotationClass, T annotation, int classRedefinedCount) throws Exception {
		Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) AnnotationData_annotations.get(annotationData);
		Map<Class<? extends Annotation>, Annotation> declaredAnnotations= (Map<Class<? extends Annotation>, Annotation>) class_declaredAnotations.get(annotationData);

		Map<Class<? extends Annotation>, Annotation> newDeclaredAnnotations = new LinkedHashMap<>(annotations);
		newDeclaredAnnotations.put(annotationClass, annotation);
		Map<Class<? extends Annotation>, Annotation> newAnnotations ;
		if (declaredAnnotations == annotations) {
			newAnnotations = newDeclaredAnnotations;
		} else{
			newAnnotations = new LinkedHashMap<>(annotations);
			newAnnotations.put(annotationClass, annotation);
		}
		return AnnotationData_constructor.newInstance(newAnnotations, newDeclaredAnnotations, classRedefinedCount);
	}
}
