package org.qiunet.data.core.mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.qiunet.data.core.mongo.annotation.MongoDbEntity;

/***
 *
 * @author qiunet
 * 2024/3/2 16:06
 ***/
@MongoDbEntity(db = "global", comment = "玩家全局表")
public class PlayerGlobalInfoDo implements IMongoEntity<Long> {
	@BsonId
	private long id;

	private String name;

	private boolean online;

	private String icon;

	private GenderType gender;

	private int level;

	public static PlayerGlobalInfoDo valueOf(long playerId, String name, boolean online, String icon, GenderType gender, int level){
		PlayerGlobalInfoDo data = new PlayerGlobalInfoDo();
		data.id = playerId;
	    data.online = online;
		data.gender = gender;
		data.level = level;
		data.name = name;
		data.icon = icon;
		return data;
	}

	@Override
	public Long getId() {
		return id;
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
