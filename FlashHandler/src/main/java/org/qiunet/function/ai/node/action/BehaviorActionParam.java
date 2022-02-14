package org.qiunet.function.ai.node.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 行为节点描述
 * @author qiunet
 * 2022/2/21 21:15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BehaviorActionParam {
	/**
	 * 参数描述
	 * @return
	 */
	String value();

	/**
	 * 字符串情况下.
	 * 可以有正则表达式限定
	 * @return
	 */
	String regex() default "";

	/**
	 * 字符长度 或者 数值
	 * 最小值
	 * @return
	 */
	long min() default 0;

	/**
	 * 字符长度 或者 数值
	 * 最大值
	 * @return
	 */
	long max() default 0;
}
