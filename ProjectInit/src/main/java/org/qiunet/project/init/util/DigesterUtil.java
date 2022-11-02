package org.qiunet.project.init.util;

import org.apache.commons.digester3.Digester;

/***
 *
 *
 * qiunet
 * 2019-08-19 16:14
 ***/
public class DigesterUtil {

	/**
	 * 对digester处理
	 * @param pattern xml 路径内容
	 * @param clazz 对应的class
	 * @param setNext 往上层路径传入的方法
	 */
	public static void addObjectCreate(Digester digester, String pattern, Class<?> clazz, String setNext) {
		digester.addObjectCreate(pattern, clazz);
		digester.addSetProperties(pattern);
		digester.addSetNext(pattern,setNext);
	}
}
