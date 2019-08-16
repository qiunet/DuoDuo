package org.qiunet.entity2table.constants;


import org.qiunet.entity2table.annotation.LengthCount;

import java.util.Date;

/**
 * 用于配置Mysql数据库中类型，并且该类型需要设置几个长度
 * 这里配置多少个类型决定了，创建表能使用多少类型
 * 例如：varchar(1)
 * double(5,2)
 * datetime
 */
public class MySqlTypeConstant {

	@LengthCount
	public static final String INT = "int";

	@LengthCount
	public static final String VARCHAR = "varchar";

	@LengthCount(LengthCount = 0)
	public static final String TEXT = "text";

	@LengthCount(LengthCount = 0)
	public static final String DATETIME = "datetime";

	@LengthCount(LengthCount = 2)
	public static final String DECIMAL = "decimal";

	@LengthCount(LengthCount = 2)
	public static final String DOUBLE = "double";

	@LengthCount
	public static final String CHAR = "char";

	/**
	 * 等于java中的long
	 */
	@LengthCount
	public static final String BIGINT = "bigint";


	/**
	 * MySQL 的布尔类型是 tinyint(1)，0 表示 false 1表示 true
	 */
	@LengthCount
	public static final String TINYINT = "tinyint";

	/**
	 * 根据属性类型,转换一个mysql类型
	 * @param type
	 * @return
	 */
	public static String parse(Class<?> type, int length){

		if (type == Integer.TYPE || type == Integer.class) return INT;
		if (type == Boolean.TYPE || type == Boolean.class) return TINYINT;
		if (type == Long.TYPE || type == Long.class) return BIGINT;
		if (type == Double.TYPE || type == Double.class) return DOUBLE;
		if (type == String.class) {
			if(length <= 255){
				return VARCHAR;
			}else{
				return TEXT;
			}
		}

		if (type == Date.class) return DATETIME;

		throw new RuntimeException("mysql po2table not define convert for type ["+type.getName()+"]");
	}

	/**
	 * 根据属性类型, 取他的长度
	 * @param type
	 * @param length
	 * @return
	 */
	public static int getLength(Class<?> type, int length){
		//0是默认值, 根据类型, 修改默认值
		if(length == 0){
			if (type == Integer.TYPE || type == Integer.class) return 10;
			if (type == Boolean.TYPE || type == Boolean.class) return 1;
			if (type == Long.TYPE || type == Long.class) return 64;
			if (type == Double.TYPE || type == Double.class) return 64;
			if (type == String.class) return 255;
			if (type == Date.class) return 10;
		}else{
			return length;
		}
		return 0;
	}

}
