package org.qiunet.data.db.support.info;


/**
 * 如果有特殊的, 可以自行实现接口
 * @author qiunet
 *         Created on 17/1/6 09:40.
 */
public interface IPlatformEntityDbInfo extends IEntityDbInfo {
	/***
	 * 得到 PlatformType Name
	 * @return  PlatformType的name
	 */
	public String getPlatformName();
}
