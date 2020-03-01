package org.qiunet.data.core.support.db;

import org.qiunet.data.core.enums.ColumnJdbcType;

import java.lang.annotation.*;

/**
 * 建表的必备注解
 *
 */
// 该注解用于方法声明
@Target(ElementType.FIELD)
// VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
// 将此注解包含在javadoc中
@Documented
// 允许子类继承父类中的注解
@Inherited
public @interface Column {
	/**
	 * 字段类型
	 *
	 * @return
	 */
	ColumnJdbcType jdbcType() default ColumnJdbcType.NULL;


	/**
	 * 是否为可以为null，true是可以，false是不可以，默认为true
	 * @return
	 */
	boolean isNull() default true;

	/**
	 * 是否是主键，默认false,不能和 isUnique 一起使用
	 * @return
	 */
	boolean isKey() default false;

	/**
	 * 是否自动递增，默认false 只有主键才能使用
	 * @return
	 */
	boolean isAutoIncrement() default false;

	/**
	 * 默认值，默认为null
	 *
	 * @return
	 */
	String defaultValue() default "NULL";

	/**
	 * 注释说明
	 * @return
	 */
	String comment() default "NULL";
}
