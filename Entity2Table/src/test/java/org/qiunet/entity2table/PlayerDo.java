package org.qiunet.entity2table;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
* , comment="玩家存放数据的表"*
* 对象为自动创建 不要修改
*/
@Alias("PlayerDo")
@Table(name = "player", comment="玩家存放数据的表")
public class PlayerDo {
	@Column(comment = "玩家id", isKey = true)
	private long uid;
	@Column(comment = "玩家名称")
	private String name;
	@Column(comment = "等级")
	private int level;
	@Column(comment = "经验")
	private long exp;

	/**默认的构造函数**/
	public PlayerDo(){}
	public PlayerDo(long uid){
		this.uid = uid;
	}
	public PlayerDo(long uid,int level){
		this.uid = uid;
		this.level = level;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}
}
