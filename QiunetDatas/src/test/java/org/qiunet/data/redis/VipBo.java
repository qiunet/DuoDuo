package org.qiunet.data.redis;

import org.qiunet.data.support.IEntityBo;

public class VipBo implements IEntityBo<VipDo> {

	private final VipDo vipDo;

	VipBo(VipDo vipDo) {
		this.vipDo = vipDo;
	}

	@Override
	public VipDo getDo() {
		return vipDo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
