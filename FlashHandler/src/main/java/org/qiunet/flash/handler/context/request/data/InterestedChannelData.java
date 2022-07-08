package org.qiunet.flash.handler.context.request.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 感兴趣的包.
 * 如果channel是等待重连状态.
 * 这个数据保存. 等重连了再发.
 *
 * @author qiunet
 * 2022/7/7 10:05
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InterestedChannelData {
}
