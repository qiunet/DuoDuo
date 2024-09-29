package org.qiunet.utils.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 *
 * @author qiunet
 * 2020-03-01 16:19
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
	/***
	 * 为事件处理提供一个先后顺序.
	 * 越大. 越靠前执行.
	 * 越小, 越靠后执行.
	 * 可以负数
	 * @return
	 */
	EventHandlerWeightType value() default EventHandlerWeightType.NORMAL;
	/**
	 * 执行次数
	 * 0 不限制
	 * @return
	 */
	int limitCount() default 0;
}
