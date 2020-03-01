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
	 * 是否是默认db的表.
	 * 是的话, 就不会按照分库的方式搞.
	 * 否则 会判断是否有分库, 没有才会查找默认db
	 * @return
	 */
	boolean defaultDb() default false;
	/**
	 * 需要分表的 {@link org.qiunet.data.core.entity.IEntityList}
	 * 加上这个注解 {@link Table#splitTable()} = true
	 * @return
	 */
	boolean splitTable() default false;
	/**
	 * 注释说明
	 *
	 * @return
	 */
	String comment() default "NULL";
}
