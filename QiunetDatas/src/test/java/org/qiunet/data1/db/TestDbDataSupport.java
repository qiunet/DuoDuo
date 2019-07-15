package org.qiunet.data1.db;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.support.DbDataSupport;

public class TestDbDataSupport {

	private static DbDataSupport<Long, PlayerPo> dataSupport = new DbDataSupport<>(PlayerPo.class);

	@Test
	public void test() {
		long uid = 10000;
		String name = "秋阳";
		PlayerPo playerPo = new PlayerPo();
		playerPo.setExp(1111111111111L);
		playerPo.setName("秋阳1");
		playerPo.setLevel(10);
		playerPo.setUid(uid);
		playerPo.insert();

		playerPo.setName(name);
		playerPo.setLevel(100);
		playerPo.update();

		playerPo = dataSupport.getPo(uid);
		Assert.assertEquals(playerPo.getName(), name);
		Assert.assertEquals(playerPo.getLevel(), 100);

		playerPo.delete();
		playerPo = dataSupport.getPo(uid);
		Assert.assertNull(playerPo);
	}
}
