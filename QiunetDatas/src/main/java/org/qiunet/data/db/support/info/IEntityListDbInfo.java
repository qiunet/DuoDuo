package org.qiunet.data.db.support.info;

/**
 * 如果有特殊的, 可以自己实现该接口.
 * @author qiunet
 *         Created on 17/1/6 09:39.
 */
public interface IEntityListDbInfo<SubKey> extends IEntityDbInfo {
	/**
	 * 得到TbIndex
	 * @return 得到table的index
	 */
	public int getTbIndex();
	/***
	 * 得到subId
	 * @return 得到 能决定
	 */
	public SubKey getSubId();
}
