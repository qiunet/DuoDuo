package org.qiunet.data1.db;

import org.qiunet.data1.support.IEntityBo;

public class ItemBo implements IEntityBo<ItemPo> {

	private ItemPo itemPo;

	ItemBo(ItemPo itemPo) {
		this.itemPo = itemPo;
	}
	@Override
	public ItemPo getPo() {
		return itemPo;
	}

	@Override
	public void serialize() {

	}

	@Override
	public void deserialize() {

	}
}
