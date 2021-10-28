package org.qiunet.entity2table;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
* *
* 对象为自动创建 不要修改
*/
@Alias("EquipDo")
@Table(name = "equip", dbSource = "basic")
public class EquipDo {
	@Column(comment = "玩家id", isKey = true)
	private long uid;
	@Column(comment = "装备id", isKey = true)
	private int equip_id;
	@Column(comment = "等级", defaultValue="0" , isNull = false)
	private int level;

	/**默认的构造函数**/
	public EquipDo(){}
	public EquipDo(long uid){
		this.uid = uid;
	}
	public EquipDo(long uid, int equip_id){
		this.uid = uid;
		this.equip_id = equip_id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getEquip_id() {
		return equip_id;
	}

	public void setEquip_id(int equip_id) {
		this.equip_id = equip_id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
