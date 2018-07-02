package org.qiunet.data.db.support.info.idInfo;

import org.qiunet.data.db.support.info.base.number.NumberEntityListDbInfo;

/**
 * Created by qiunet.
 * 17/6/20
 */
public class IdEntityListDbInfo extends NumberEntityListDbInfo {
	private int id;
	public IdEntityListDbInfo(Object id, int subId) {
		super((Integer) id, subId);
		this.id = (int) id;
	}

	public int getId() {
		return id;
	}
}
