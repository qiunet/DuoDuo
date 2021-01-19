package org.qiunet.data.core.support.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 创建表时的表名
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
	/**
	 * 适用哪个数据源名.
	 * 如果 serverType == 0. 取默认数据源
	 * @return
	 */
	String dbSource() default "";
}
