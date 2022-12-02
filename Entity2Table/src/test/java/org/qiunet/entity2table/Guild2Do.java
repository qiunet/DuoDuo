package org.qiunet.entity2table;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.core.enums.ColumnJdbcType;
import org.qiunet.data.core.support.db.Column;
import org.qiunet.data.core.support.db.Table;

/**
* *
* 对象为自动创建 不要修改
*/
@Alias("GuildDo")
@Table(name = "guild", keyName = "guildId", dbSource = "basic")
public class Guild2Do {
	@Column(comment = "公会id", isKey = true)
	private long guildId;
	@Column(comment = "公会名称", jdbcType = ColumnJdbcType.VARCHAR1000)
	private String name;
	@Column(comment = "公会等级")
	private int level;

	/**默认的构造函数**/
	public Guild2Do(){}
	public Guild2Do(long guildId){
		this.guildId = guildId;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
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

}
