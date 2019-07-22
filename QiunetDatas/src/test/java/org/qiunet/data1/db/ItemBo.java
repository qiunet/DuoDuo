package org.qiunet.data1.db;

import org.qiunet.data1.support.IEntityBo;

public class ItemBo implements IEntityBo<ItemDo> {

	private ItemDo itemDo;

	ItemBo(ItemDo itemDo) {
		this.itemDo = itemDo;
	}
	@Override
	public ItemDo getDo() {
		return itemDo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
