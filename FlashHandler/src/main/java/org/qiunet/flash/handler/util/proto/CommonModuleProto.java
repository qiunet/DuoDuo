package org.qiunet.flash.handler.util.proto;

import java.lang.annotation.*;

/***
 * {@link ProtoGeneratorModel#GROUP_BY_MODULE}
 * {@link ProtoGeneratorModel#ONE_PROTOCOL_ONE_FILE} 模式下.
 *
 * 数据结构多个(模块/协议)需要使用
 * 扔到 common module
 * 避免 (非本模块/协议) 协议变动导致冲突
 *
 * 用注解可控一点. 如果自动放common, 会可能导致客户端的协议包变来变去.
 *
 * @author qiunet
 * 2022/5/18 18:03
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonModuleProto {
}
