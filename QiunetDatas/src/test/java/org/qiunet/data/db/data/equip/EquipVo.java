package org.qiunet.data.db.data.equip;

import org.qiunet.data.redis.entity.EquipPo;

/**
 * @author qiunet
 *         Created on 17/2/14 10:43.
 */
public class EquipVo {
	
	private EquipPo equipPo;
	
	public EquipVo(EquipPo equipPo) {
		this.equipPo = equipPo;
	}
	
	public EquipPo getEquipPo() {
		return equipPo;
	}
}
