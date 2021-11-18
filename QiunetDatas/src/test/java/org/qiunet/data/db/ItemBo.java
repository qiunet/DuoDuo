package org.qiunet.data.db;

import org.qiunet.data.db.loader.DbEntityBo;

public class ItemBo extends DbEntityBo<ItemDo> {

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
