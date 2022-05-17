package org.qiunet.flash.handler.context.request.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 *
 *
 * @author qiunet
 * 2020-09-21 15:35
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ChannelData {
	/**
	 * 协议id
	 * @return
	 */
	int ID();

	/**
	 * 描述
	 * @return
	 */
	String desc();
	/**
	 * 表示是udp协议. 使用kcp发送解析.
	 * 使用什么发送解析业务自己处理.
	 *
	 * 这里仅仅是给客户端生成文档使用, 不会帮业务自动使用kcp发送.
	 * @return
	 */
	boolean kcp() default false;
}
