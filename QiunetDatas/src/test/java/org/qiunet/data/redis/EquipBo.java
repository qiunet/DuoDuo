package org.qiunet.data.redis;

import org.qiunet.data.support.IEntityBo;

public class EquipBo implements IEntityBo<EquipDo> {

	private EquipDo equipDo;

	EquipBo(EquipDo equipDo) {
		this.equipDo = equipDo;
	}

	@Override
	public EquipDo getDo() {
		return equipDo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
