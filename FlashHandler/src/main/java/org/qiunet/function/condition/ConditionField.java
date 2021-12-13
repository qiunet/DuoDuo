package org.qiunet.function.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 给条件字段注解
 *
 * @author qiunet
 * 2021/12/13 11:03
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionField {
	/**
	 * 描述 给策划AI 编辑使用
	 * @return
	 */
	String desc();
}
