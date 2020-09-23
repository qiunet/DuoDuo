package org.qiunet.tests.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/***
 *
 *
 * @author qiunet
 * 2020-09-23 10:21
 */
public class Item {
	@Protobuf(description = "物品id")
	private int itemId;
	@Protobuf(description = "物品数量")
	private int count;

	public static Item valueOf(int itemId, int count) {
		Item item = new Item();
		item.itemId = itemId;
		item.count = count;
		return item;
	}

	public int getItemId() {
		return itemId;
	}

	public int getCount() {
		return count;
	}
}
