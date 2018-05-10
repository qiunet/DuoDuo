package org.qiunet.data.db.dao;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.entity.PlayerPo;

/**
 * @author qiunet
 *         Created on 17/1/24 15:27.
 */
public class TestPlayerDao {
	@Test
	public void testPlayerDao(){
		PlayerPo playerPo = new PlayerPo();
		playerPo.setPlatform(PlatformType.ANDROID);
		playerPo.setUid(1000);
		playerPo.setExp(100);
		playerPo.setLevel(10);
		PlayerDaoImpl.getInstance().insertPlayerPo(playerPo);

		playerPo = PlayerDaoImpl.getInstance().getPlayerPo(1000, PlatformType.ANDROID);
		playerPo.setPlatform(PlatformType.ANDROID);
		Assert.assertTrue(playerPo != null && playerPo.getExp() == 100);

		PlayerDaoImpl.getInstance().deletePlayerPo(playerPo);

		playerPo = PlayerDaoImpl.getInstance().getPlayerPo(1000, PlatformType.ANDROID);
		Assert.assertNull(playerPo);
	}
}
