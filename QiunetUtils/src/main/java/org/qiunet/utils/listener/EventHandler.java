package org.qiunet.utils.listener;

import java.lang.annotation.*;

/**
 * 给 {@link IEventListener#eventHandler(IEventData)}方法注解
 */
@Target({ElementType.METHOD})
@Repeatable(_EventHandlers.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	/***
	 * 这里应该填枚举值. 但是java限制很多
	 * 只能使用常量.
	 * @return
	 */
	Class<? extends IEventData> value();
	/***
	 * 两个相同的EventHandler 会把权重相加
	 * 执行的顺序 大的先执行
	 * @return
	 */
	int weight() default 0;
}
