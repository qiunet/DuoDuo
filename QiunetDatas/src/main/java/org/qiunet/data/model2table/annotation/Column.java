package org.qiunet.data.model2table.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
	 * 字段长度，默认是255
	 *
	 * @return 字段长度，默认是255
	 */
	int length() default 0;

	/**
	 * 小数点长度，默认是0
	 *
	 * @return 小数点长度，默认是0
	 */
	int decimalLength() default 0;

	/**
	 * 是否为可以为null，true是可以，false是不可以，默认为true
	 *
	 * @return 是否为可以为null，true是可以，false是不可以，默认为true
	 */
	boolean isNull() default true;

	/**
	 * 是否是主键，默认false,不能和 isUnique 一起使用
	 *
	 * @return 是否是主键，默认false
	 */
	boolean isKey() default false;

	/**
	 * 是否自动递增，默认false 只有主键才能使用
	 *
	 * @return 是否自动递增，默认false 只有主键才能使用
	 */
	boolean isAutoIncrement() default false;

	/**
	 * 默认值，默认为null
	 *
	 * @return 默认值，默认为null
	 */
	String defaultValue() default "NULL";

	/**
	 * 是否是唯一，默认false
	 *
	 * @return 是否是唯一，默认false
	 */
	boolean isUnique() default false;

	/**
	 * 是否无符号，只能用在数值类型的字段中
	 *
	 * @return 是否是唯一，默认false
	 */
	boolean isUnsigned() default false;

	/**
	 * 注释说明
	 * @return
	 */
	String comment() default "NULL";
}
