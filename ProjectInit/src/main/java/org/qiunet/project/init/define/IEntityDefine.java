package org.qiunet.project.init.define;

import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-08-14 20:34
 ***/
public interface IEntityDefine {
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
	/***
	 * 得到字段的定义
	 * @return
	 */
	List<FieldDefine> getFieldDefines();
}
