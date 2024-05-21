package org.qiunet.data.mongo;

import org.qiunet.data.mongo.annotation.DbRef;
import org.qiunet.data.mongo.annotation.MongoDbEntity;

/***
 *
 * @author qiunet
 * 2024/2/22 15:54
 ***/
@MongoDbEntity
public class PlayerDo extends BasicPlayerMongoEntity {

	@DbRef
	private PlayerGlobalInfoDo globalInfoDo;

	private Purse purse;

	public PlayerDo() {
	}

	public PlayerDo(Long id) {
		super(id);
	}

	public static PlayerDo find(long id) {
		return IMongoEntity._find(PlayerDo.class, id);
	}

	public static PlayerDo valueOf(long playerId, GenderType gender, String name, int level, long m1, long m2){
		PlayerDo data = new PlayerDo(playerId);
		data.globalInfoDo = PlayerGlobalInfoDo.valueOf(playerId, name, true, "icon", gender, level);
		data.purse = Purse.valueOf(m1, m2);
		return data;
	}

	public PlayerGlobalInfoDo getGlobalInfoDo() {
		return globalInfoDo;
	}

	public Purse getPurse() {
		return purse;
	}
}
