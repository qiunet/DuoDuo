package org.qiunet.data.db.support.info.glboal;

import org.qiunet.data.db.support.info.glboal.base.BaseGlobalDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/27 18:26.
 */
public class IdGlobalDbInfo extends BaseGlobalDbInfo{
	private int id;
	
	public IdGlobalDbInfo(Object id) {
		this.id = (Integer) id;
	}
	
	public int getId() {
		return id;
	}
}
