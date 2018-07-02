package org.qiunet.data.db.support.info.idInfo;

import org.qiunet.data.db.support.info.base.number.NumberEntityDbInfo;

/**
 * 一般id 不包含uid等, 所以利用id分库
 * @author qiunet
 *         Created on 17/2/24 18:22.
 */
public class IdEntityDbInfo extends NumberEntityDbInfo {
	private int id;
	public IdEntityDbInfo (Object id) {
		super((Integer) id);
		this.id = (Integer)id;
	}

	public int getId() {
		return id;
	}
}
