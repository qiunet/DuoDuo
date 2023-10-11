package org.qiunet.flash.handler.context.request.check.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 字符串参数检查注解
 *
 * @author qiunet
 * 2022/1/5 17:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringParam {
	/**
     * 自定义最小值
	 * @return
     */
	long min() default 0;
	/**
	 * 自定义最大值
	 * @return
	 */
	long max() default 0;
	/**
	 * 自定义最小值 从key val 表读取
	 * 优先级 比{@link #min()} 高
	 * @return
	 */
	String minKey() default "";
	/**
	 * 自定义最大值 从key val 表读取
	 * 优先级 比{@link #max()} 高
	 * @return
	 */
	String maxKey() default "";
	/**
	 * 检查关键字
	 * @return
	 */
	boolean checkBadWord() default false;

	/**
	 * 去除两端空格
	 * @return
	 */
	boolean trim() default false;
	/**
	 * 只保留中英文+数字
	 * @return
	 */
	boolean powerTrim() default false;
	/**
	 * 不能为 空
	 * @return
	 */
	boolean checkEmpty() default false;
	/**
	 * 按照中文2个长度校验.
	 * @return
	 */
	boolean cnCheck() default false;

}
