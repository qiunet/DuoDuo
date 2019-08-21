package org.qiunet.entity2table.utils;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 通过包名获取class
 */
public class ClassTools {

	/**
	 * 取出list对象中的某个属性的值作为list返回
	 *
	 * @param objList
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> List<E> getPropertyValueList(List<T> objList, String fieldName) {
		return objList.stream().map(t -> {
			try {
				Field field = t.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				return (E)field.get(t);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				e.printStackTrace();
			}
			return null;
		}).filter(Objects::nonNull)
		.collect(Collectors.toList());
	}
}
