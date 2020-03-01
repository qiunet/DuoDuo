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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
	/***
	 * 为eventData指定一个监听使用的接口
	 * 所有对该事件感兴趣的service都实现该接口.
	 * ListenerManager 会在同线程自动调用接口的方法.
	 * @return
	 */
	Class<?> value();
}
