package org.qiunet.data.core.mongo.annotation;

import org.qiunet.data.enums.ServerType;

import java.lang.annotation.*;

/***
 * 指定某个对象为mongo 数据对象
 * @author qiunet
 * 2023/8/23 11:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MongoDbEntity {
	/**
	 * entity 作用范围
	 * 其它服务类型不允许调用
	 * @return 服务类型组
	 */
	ServerType [] serverTypes() default ServerType.LOGIC;
	/**
	 * 数据库使用的 dbSource
	 * 默认使用basic, 不同的需要在这里指定
	 * @return db source
	 */
	String dbSource() default "basic";

	/**
	 * db name
	 *
	 * @return db name 默认db source 配置的db
	 */
	String db() default "";

	/**
	 * collection name
	 *
	 * @return 默认当前class name 去掉Entity 然后转小写
	 */
	String collection() default "";

	/**
	 * @return 表的描述
	 */
	String comment();
}
