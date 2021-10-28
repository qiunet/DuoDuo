package org.qiunet.entity2table;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
* *
* 对象为自动创建 不要修改
*/
@Alias("VipDo")
@Table(name = "vip", dbSource = "basic")
public class VipDo  {
	@Column(comment = "玩家id", isKey = true)
	private long uid;
	@Column(comment = "等级")
	private int level;
	@Column(comment = "经验")
	private int exp;

	/**默认的构造函数**/
	public VipDo(){}
	public VipDo(long uid){
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}


}
