package org.qiunet.data.db.support.info.idInfo;

import org.qiunet.data.db.support.info.base.number.NumberPlatformEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/2/10 15:31.
 */
public class IdPlatformEntityListDbInfo extends NumberPlatformEntityListDbInfo {
	private int id;

	public IdPlatformEntityListDbInfo(Object id, PlatformType platformType) {
		this(id, platformType, 0);
	}

	public IdPlatformEntityListDbInfo(Object id, PlatformType platformType, int subId) {
		super((Integer) id, platformType, subId);
		this.id = (int) id;
	}

	public int getId() {
		return id;
	}
}
