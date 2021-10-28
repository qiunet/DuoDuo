package org.qiunet.entity2table;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
* *
* 对象为自动创建 不要修改
*/
@Alias("ItemDo")
@Table(name = "item", dbSource = "basic")
public class ItemDo {
	@Column(comment = "玩家id", isKey = true)
	private long uid;
	@Column(comment = "道具id", isKey = true)
	private int item_id;
	@Column(comment = "数量")
	private int count;

	/**默认的构造函数**/
	public ItemDo(){}
	public ItemDo(long uid){
		this.uid = uid;
	}
	public ItemDo(long uid, int item_id){
		this.uid = uid;
		this.item_id = item_id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
