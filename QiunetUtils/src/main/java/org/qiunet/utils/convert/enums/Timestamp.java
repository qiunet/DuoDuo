package org.qiunet.utils.convert.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 标记字段是 需要注入时间戳.
 * 会将 {@link  org.qiunet.utils.date.DateUtil#DEFAULT_DATE_TIME_FORMAT} 格式字符串自动转入时间戳
 * 仅在Long字段有效
 * @author qiunet
 * 2024/3/15 16:11
 ***/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timestamp {
	/**
	 * 是否自动带入地区偏移
	 * @return true 带入
	 */
	boolean zoned() default true;
}
