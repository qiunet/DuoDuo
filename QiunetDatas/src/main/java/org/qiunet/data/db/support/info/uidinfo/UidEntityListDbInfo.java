package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.base.number.NumberEntityListDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/10 15:30.
 */
public class UidEntityListDbInfo<SubKey> extends NumberEntityListDbInfo<SubKey> {
	private int uid;
	public UidEntityListDbInfo(int uid, SubKey subId) {
		super( uid, subId);
		this.uid = uid;
	}

	public int getUid() {
		return uid;
	}
}
