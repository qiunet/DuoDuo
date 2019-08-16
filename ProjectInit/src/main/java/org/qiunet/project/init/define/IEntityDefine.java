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
public interface IEntityDefine {
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
	 * 得到import信息
	 * @return
	 */
	List<String> getImportInfos();
	/***
	 * 得到字段的定义
	 * @return
	 */
	List<FieldDefine> getFieldDefines();
}
