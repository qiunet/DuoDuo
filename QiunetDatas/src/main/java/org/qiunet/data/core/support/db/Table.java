package org.qiunet.data.core.support.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****
 * 需要分表的 {@link org.qiunet.data.core.entity.IEntityList}
 * 加上这个注解 {@link Table#splitTable()} = true
 *
 * 主要是给自动建表判断用的。
 * 实际上list的sql语句加入 {tbIndex} 参数就行
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**
	 * 是否需要分表
	 * @return
	 */
	boolean splitTable() default false;
}
