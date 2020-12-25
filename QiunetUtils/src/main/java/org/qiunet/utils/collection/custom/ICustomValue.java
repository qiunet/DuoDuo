package org.qiunet.utils.collection.custom;

/***
 * 用户自定义的对象. 实现对象和字符串的互转
 *
 * @author qiunet
 * 2020-12-25 11:56
 */
public interface ICustomValue {
	/**
	 * 转成字符串
	 * @param obj
	 * @return
	 */
	String toString(Object obj);

	/**
	 * 解析成对象
	 * @param str
	 * @return
	 */
	Object parse(String str);

	/**
	 * 默认值
	 * @return
	 */
	Object defaultVal();
}
