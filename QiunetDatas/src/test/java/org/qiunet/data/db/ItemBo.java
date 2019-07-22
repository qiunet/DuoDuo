package org.qiunet.data.db;

import org.qiunet.data.support.IEntityBo;

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
