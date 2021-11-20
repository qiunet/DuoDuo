package org.qiunet.utils.test.jmh;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 *
 * @author qiunet
 * 2021/11/20 16:27
 */
@ProtobufClass
public class ItemObj {
	/**
	 * 物品ID
	 */
	@Protobuf
	private int itemId;
	/**
	 * 物品数量
	 */
	@Protobuf
	private int count;


	public ItemObj() {}

	public ItemObj(int itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
