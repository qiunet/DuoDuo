package org.qiunet.data.db;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.db.entity.DbEntityList;

@Alias("ItemDo")
@Table(name = "item", defaultDb = true)
public class ItemDo extends DbEntityList<Long, Integer, ItemBo> {
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
