package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.base.number.NumberEntityListDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/10 15:30.
 */
public class UidEntityListDbInfo extends NumberEntityListDbInfo {
	private int uid;
	public UidEntityListDbInfo(Object uid, int subId) {
		super((Integer) uid, subId);
		this.uid = (int) uid;
	}

	public int getUid() {
		return uid;
	}
}
