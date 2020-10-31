package org.qiunet.flash.handler.util;

import java.lang.annotation.*;

/***
 * 需要输出proto的类注解.
 * 有时候协议里面是用byte给出那种情况.
 *
 * @author qiunet
 * 2020-10-31 15:02
 **/
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedProtoGenerator {
}
