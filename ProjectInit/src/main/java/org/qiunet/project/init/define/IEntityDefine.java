package org.qiunet.project.init.define;

import org.qiunet.data.core.entity.IEntity;
import org.qiunet.project.init.enums.EntityType;

import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-08-14 20:34
 ***/
public interface IEntityDefine extends ITemplateObjectDefine {
	/***
	 * 得到表名称
	 * @return
	 */
	String getTableName();

	/***
	 * 得到 mybatis 的namespace
	 * @return
	 */
	String getNameSpace();

	/***
	 * 得到配置的packageName
	 * @return
	 */
	String getPackageName();
	/***
	 * 获得类型
	 * @return
	 */
	EntityType getType();

	/***
	 * 得到entity class
	 * @return
	 */
	Class<? extends IEntity> getEntityClass();
	/***
	 * 得到key的类型名称
	 * @return
	 */
	String getKeyType();
	/***
	 * 得到key的名称
	 * @return
	 */
	String getKeyName();
	/**
	 * 得到do 类名
	 * @return
	 */
	String getDoName();
	/***
	 * 得到service的类名
	 * @return
	 */
	String getServiceName();
	/***
	 * 得到bo 类名
	 * @return
	 */
	String getBoName();

	/***
	 * 得到构造函数的定义
	 * @return
	 */
	List<ConstructorDefine> getConstructorDefines();
	/**
	 * 得到entity的package
	 * @return
	 */
	String getEntityPackage();
	/***
	 * 得到字段的定义
	 * @return
	 */
	List<FieldDefine> getFieldDefines();
	/***
	 * 得到数据库查询语句
	 * @return
	 */
	String getSelectSql();
	/**
	 * 获得所有数据
	 * @return
	 */
	String getSelectAllSql();
	/**
	 * 得到数据库插入语句
	 * @return
	 */
	String getInsertSql();

	/***
	 * 得到数据库更新语句
	 * @return
	 */
	String getUpdateSql();

	/***
	 * 得到数据库删除语句
	 * @return
	 */
	String getDeleteSql();

	/**
	 * 得到表的注释
	 * @return
	 */
	String getComment();

	/**
	 * 是否是列表类型
	 * @return
	 */
	boolean isList();
}
