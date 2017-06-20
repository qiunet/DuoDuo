package org.qiunet.data.db.support.info.idInfo;

import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.db.util.DbProperties;

/**
 * Created by qiunet.
 * 17/6/20
 */
public class IdEntityListDbInfo extends IdEntityDbInfo implements IEntityListDbInfo {
	private int subId;
	private int tbIndex;

	public IdEntityListDbInfo(Object id, int subId) {
		super(id);
		this.subId = subId;
		this.tbIndex = DbProperties.getInstance().getTbIndexById(getId());
	}

	@Override
	public int getTbIndex() {
		return tbIndex;
	}

	@Override
	public int getSubId() {
		return subId;
	}
}
