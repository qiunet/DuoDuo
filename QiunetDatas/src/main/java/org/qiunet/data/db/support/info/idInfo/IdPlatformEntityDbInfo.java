package org.qiunet.data.db.support.info.idInfo;

import org.qiunet.data.db.support.info.base.number.NumberPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/1/24 16:50.
 */
public class IdPlatformEntityDbInfo extends NumberPlatformEntityDbInfo {
	private int id;
	public IdPlatformEntityDbInfo(Object id, PlatformType platformType) {
		super((Integer) id, platformType);
		this.id = (int) id;
	}

	public int getId() {
		return id;
	}
}
