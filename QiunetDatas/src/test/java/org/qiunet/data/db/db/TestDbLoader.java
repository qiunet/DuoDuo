package org.qiunet.data.db.db;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.core.loader.DbLoader;
import org.qiunet.data.db.data.player.PlayerEntityInfo;
import org.qiunet.data.db.datasource.DataSourceType;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.entity.PlayerPo;

public class TestDbLoader{

	@Test
	public void testDbLoader(){
		PlatformType platform = PlatformType.IOS;
		PlayerEntityInfo entityInfo = new PlayerEntityInfo();
		String datasourceType = DataSourceType.DATASOURCE_PLAYER+"0";
		SqlSession sqlSession = DbLoader.getInstance().getSqlSession(datasourceType);

		PlayerPo playerPo = new PlayerPo();
		playerPo.setUid(1100);
		playerPo.setExp(100);
		playerPo.setLevel(10);
		playerPo.setPlatform(platform);
		playerPo.setEntityDbInfo(entityInfo.getEntityDbInfo(playerPo));
		sqlSession.insert("player.insertPlayerPo", playerPo);

		sqlSession = DbLoader.getInstance().getSqlSession(datasourceType);
		PlayerPo playerPo1 = sqlSession.selectOne("player.getPlayerPo", entityInfo.getEntityDbInfo(playerPo.getUid(), platform));
		playerPo1.setPlatform(platform);
		Assert.assertEquals(playerPo1.getLevel(), 10);

		sqlSession = DbLoader.getInstance().getSqlSession(datasourceType);
		playerPo1.setEntityDbInfo(entityInfo.getEntityDbInfo(playerPo1));
		sqlSession.delete("player.deletePlayerPo", playerPo1);

		sqlSession = DbLoader.getInstance().getSqlSession(datasourceType);
		PlayerPo playerPo2 = sqlSession.selectOne("player.getPlayerPo", entityInfo.getEntityDbInfo(playerPo.getUid(), platform));
		Assert.assertNull(playerPo2);
	}
}
