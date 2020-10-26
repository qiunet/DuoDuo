package org.qiunet.flash.handler.common.annotation;

import java.lang.annotation.*;

/***
 * 标识转发的handler
 * 如果是跨服的状态.
 * handler处理会直接转发到跨服服务.
 *
 * 因为依赖问题. 没法直接找到cross的BaseTcpPbTransmitHandler. 所以用注解中间解决.
 * 业务不要使用该注解. 直接使用Cross模块的 BaseTcpPbTransmitHandler, BaseWsPbTransmitHandler
 * @author qiunet
 * 2020-10-26 15:09
 */
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TransmitHandler {
}
