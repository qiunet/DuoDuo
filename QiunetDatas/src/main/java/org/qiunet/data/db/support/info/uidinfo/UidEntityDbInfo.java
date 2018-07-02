package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.base.number.NumberEntityDbInfo;

/**
 * @author qiunet
 *         Created on 17/1/24 16:10.
 */
public class UidEntityDbInfo extends NumberEntityDbInfo {
	private int uid;
	public UidEntityDbInfo( Object uid) {
		super((Integer) uid);
		this.uid = (Integer)uid;
	}
	public int getUid(){
		return uid;
	}
}
