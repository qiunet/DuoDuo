package org.qiunet.data1.util;

import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.utils.date.DateUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * Qiunet Data 里面操作交互数据的工具类
 * @Author qiunet
 * @Date Create in 2018/10/31 10:22
 **/
public class DataUtil {
	/***
	 * 取到doName 对应的nameSpace
	 * @param doName
	 * @return
	 */
	public static String getNameSpace(String doName) {
		if (doName.endsWith("Do")) {
			return doName.substring(0, doName.length() - 2).toLowerCase();
		}
		return doName;
	}
	/***
	 * 得到表名 保留驼峰.
	 * @param doName
	 * @return
	 */
	public static String getDefaultTableName(String doName) {
		if (doName.endsWith("Do")) {
			return doName.substring(0, doName.length() - 2);
		}
		return doName;
	}
}
