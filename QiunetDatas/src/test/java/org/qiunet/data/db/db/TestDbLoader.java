package org.qiunet.data.db.db;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.data.player.PlayerEntityInfo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.entity.PlayerPo;

public class TestDbLoader{

	@Test
	public void testDbLoader(){
		PlatformType platform = PlatformType.IOS;
		PlayerEntityInfo entityInfo = new PlayerEntityInfo();

		PlayerPo playerPo = new PlayerPo();
		playerPo.setUid(1100);
		playerPo.setExp(100);
		playerPo.setLevel(10);
		playerPo.setPlatform(platform);
		playerPo.setEntityDbInfo(entityInfo.getEntityDbInfo(playerPo));
		DatabaseSupport.getInstance().insert(playerPo.getDbSourceKey(),"player.insertPlayerPo", playerPo);

		PlayerPo playerPo1 = DatabaseSupport.getInstance().selectOne(playerPo.getDbSourceKey(),"player.getPlayerPo", entityInfo.getEntityDbInfo(playerPo.getUid(), platform));
		playerPo1.setPlatform(platform);
		Assert.assertEquals(playerPo1.getLevel(), 10);

		playerPo1.setEntityDbInfo(entityInfo.getEntityDbInfo(playerPo1));
		DatabaseSupport.getInstance().delete(playerPo.getDbSourceKey(),"player.deletePlayerPo", playerPo1);

		PlayerPo playerPo2 = DatabaseSupport.getInstance().selectOne(playerPo.getDbSourceKey(),"player.getPlayerPo", entityInfo.getEntityDbInfo(playerPo.getUid(), platform));
		Assert.assertNull(playerPo2);
	}
}
