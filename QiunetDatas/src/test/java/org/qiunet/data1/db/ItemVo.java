package org.qiunet.data1.db;

import org.qiunet.data1.support.IEntityVo;

public class ItemVo implements IEntityVo<ItemPo> {

	private ItemPo itemPo;

	public ItemVo(ItemPo itemPo) {
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
