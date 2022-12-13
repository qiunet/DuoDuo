package org.qiunet.quartz.convert;

import org.qiunet.quartz.CronExpression;
import org.qiunet.utils.convert.BaseObjConvert;
import org.qiunet.utils.exceptions.CustomException;

import java.lang.reflect.Field;
import java.text.ParseException;

/***
 * 转换cron 表达式
 * @author qiunet
 * 2022/12/26 14:36
 */
public class CronConvert extends BaseObjConvert<CronExpression> {
	@Override
	public CronExpression fromString(Field field, String str) {
		try {
			return new CronExpression(str);
		} catch (ParseException e) {
			throw new CustomException(e, "parse exception");
		}
	}
}
