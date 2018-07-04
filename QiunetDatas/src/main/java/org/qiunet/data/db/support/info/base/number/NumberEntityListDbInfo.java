package org.qiunet.data.db.support.info.base.number;

import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.db.util.DbProperties;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:35
 **/
public abstract class NumberEntityListDbInfo<SubKey> extends NumberEntityDbInfo implements IEntityListDbInfo<SubKey> {
	private SubKey subId;
	private int tbIndex;

	public NumberEntityListDbInfo(int val, SubKey subId) {
		super(val);
		this.subId = subId;
		this.tbIndex = DbProperties.getInstance().getTbIndexById(val);
	}

	@Override
	public int getTbIndex() {
		return tbIndex;
	}

	@Override
	public SubKey getSubId() {
		return subId;
	}
}
