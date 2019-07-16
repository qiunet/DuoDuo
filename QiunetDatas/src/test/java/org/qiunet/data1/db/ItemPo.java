package org.qiunet.data1.db;

import org.apache.ibatis.type.Alias;
import org.qiunet.data1.db.entity.DbEntityList;

@Alias("ItemPo")
public class ItemPo extends DbEntityList<Long, Integer, ItemVo> {
	private long uid;
	private int item_id;
	private int count;

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

	@Override
	public Integer subKey() {
		return item_id;
	}

	@Override
	public String subKeyFieldName() {
		return "item_id";
	}

	@Override
	public Long key() {
		return uid;
	}

	@Override
	public String keyFieldName() {
		return "uid";
	}
}
