package org.qiunet.utils.classScanner;

import java.lang.annotation.Annotation;

/**
 * @author qiunet
 *         Created on 17/1/23 18:37.
 */
public interface IScannerHandler {
	/**
	 * 给定需要匹配的注解.
	 * 该扫描仅扫描类注解. 具体的处理在{@link IScannerHandler#handler(Class)} 中
	 * @return
	 */
	public boolean matchClazz(Class clazz);
	/***
	 * 返回对应的clazz
	 * @param clazz
	 */
	public void handler(Class<?> clazz);
}
