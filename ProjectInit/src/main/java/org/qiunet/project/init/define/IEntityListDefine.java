package org.qiunet.project.init.define;

/***
 *
 *
 * qiunet
 * 2019-08-20 10:04
 ***/
public interface IEntityListDefine extends IEntityDefine {
	/***
	 * 得到subKey 得名称
	 * @return
	 */
	String getSubKeyName();

	/***
	 * 得到subKey的类型
	 * @return
	 */
	String getSubKeyType();
	/**
	 *是否分表
	 * @return
	 */
	boolean isSplitTable();
}
