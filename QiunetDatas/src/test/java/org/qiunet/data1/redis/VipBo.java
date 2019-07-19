package org.qiunet.data1.redis;

import org.qiunet.data1.support.IEntityBo;

public class VipBo implements IEntityBo<VipDo> {

	private VipDo vipDo;

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
