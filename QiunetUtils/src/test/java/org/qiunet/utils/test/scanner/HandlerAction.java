package org.qiunet.utils.test.scanner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiunet
 *         Created on 17/1/23 19:37.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerAction {
	/**
	 * 处理的id
	 * @return
	 */
	int ID();
}
