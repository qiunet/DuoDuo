package org.qiunet.data.mongo;

import org.qiunet.data.mongo.annotation.MongoDbEntity;

/***
 *
 * @author qiunet
 * 2024/3/2 16:06
 ***/
@MongoDbEntity(db = "global")
public class PlayerGlobalInfoDo extends BasicMongoEntity<Long> {

	private String name;

	private boolean online;

	private String icon;

	private GenderType gender;

	private int level;

	public static PlayerGlobalInfoDo valueOf(long playerId, String name, boolean online, String icon, GenderType gender, int level){
		PlayerGlobalInfoDo data = new PlayerGlobalInfoDo();
		data.setId(playerId);
	    data.online = online;
		data.gender = gender;
		data.level = level;
		data.name = name;
		data.icon = icon;
		return data;
	}

	@Override
	public Long getId() {
		return super.getId();
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GenderType getGender() {
		return gender;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public boolean isOnline() {
		return online;
	}

	public String getIcon() {
		return icon;
	}
}
