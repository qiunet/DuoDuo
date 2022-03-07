package org.qiunet.function.gm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * gm 命令的参数描述
 *
 * @author qiunet
 * 2021-01-09 17:20
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface GmParamDesc {
	/**
	 * 参数在客户端的描述
	 * @return
	 */
	String value();
	/**
	 * 校验的正则表达式
	 * 填写了表达式. 需要同步填写示例.
	 * @return
	 */
	String regex() default "";
	/**
	 * 示例
	 * @return
	 */
	String example() default "";
}
