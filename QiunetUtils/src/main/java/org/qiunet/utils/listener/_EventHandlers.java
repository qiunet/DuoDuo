package org.qiunet.utils.listener;

import java.lang.annotation.*;

/**
 * 容易和EventHandler 混淆.
 * 所以加个 _
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface _EventHandlers {
	/***
	 * 这里应该填枚举值. 但是java限制很多
	 * 只能使用常量.
	 * @return
	 */
	EventHandler[] value();
}
