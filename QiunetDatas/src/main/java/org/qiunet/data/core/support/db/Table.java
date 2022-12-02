package org.qiunet.data.core.support.db;

import java.lang.annotation.*;


/**
 * 创建表时的表名
 * @author qiunet
 */
//表示注解加在接口、类、枚举等
@Target(ElementType.TYPE)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
//允许子类继承父类中的注解
@Inherited
public @interface Table {

	/**
	 * 表名
	 *
	 * @return 表名
	 */
	String name();

	/**
	 * 适用哪个数据源名.
	 * @return
	 */
	String dbSource();

	/**
	 * 主键名
	 * @return 主键名
	 */
	String keyName();

	/**
	 * 第二个主键名
	 * @return 主键名
	 */
	String subKeyName() default "NULL";
	/**
	 * 需要分表的这里为 true
	 * @return
	 */
	boolean splitTable() default false;
	/**
	 * 是否异步入库.
	 * false 为同步
	 * @return
	 */
	boolean async() default true;
	/**
	 * 注释说明
	 *
	 * @return
	 */
	String comment() default "NULL";
}
