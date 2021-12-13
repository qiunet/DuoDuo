package org.qiunet.function.ai.node.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * action的描述
 * @author qiunet
 * 2021/12/13 16:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BehaviorAction {
	/**
	 * 描述
	 * @return
	 */
	String desc();
}
