package org.qiunet.flash.handler.context.request.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 服务通讯协议.
 * 标记后 不会转发客户端
 *
 * @author qiunet
 * 2022/6/14 20:37
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerCommunicationData {
}
