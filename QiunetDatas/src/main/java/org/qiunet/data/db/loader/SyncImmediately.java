package org.qiunet.data.db.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 标注在 {@link DbEntityBo}上面, 表示更新立即落地
 *
 * @author qiunet
 * 2024/3/19 19:26
 ***/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncImmediately {
}
