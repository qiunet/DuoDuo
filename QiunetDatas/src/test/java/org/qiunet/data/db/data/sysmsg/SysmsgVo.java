package org.qiunet.data.db.data.sysmsg;

import org.qiunet.data.redis.entity.SysmsgPo;

/**
 * @author qiunet
 *         Created on 17/2/13 16:14.
 */
public class SysmsgVo {
	
	private SysmsgPo sysMsgPo;
	
	public SysmsgVo(SysmsgPo sysMsgPo) {
		this.sysMsgPo = sysMsgPo;
	}
	
	public SysmsgPo getSysMsgPo() {
		return sysMsgPo;
	}
}
