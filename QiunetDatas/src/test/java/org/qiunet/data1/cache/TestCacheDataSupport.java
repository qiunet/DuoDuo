package org.qiunet.data1.cache;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.support.CacheDataSupport;

public class TestCacheDataSupport {
	private static CacheDataSupport<Long, GuildPo, GuildVo> dataSupport = new CacheDataSupport<>(GuildPo.class, GuildVo::new);
	private long guildId = 100000;

	@Test
	public void testEntity(){
		GuildPo guildPo = new GuildPo();
		guildPo.setGuildId(guildId);
		guildPo.setName("公会1");
		guildPo.setLevel(10);
		guildPo.insert();
		dataSupport.syncToDatabase();

		guildPo.setName("公会");
		guildPo.update();
		guildPo.insert();
		dataSupport.syncToDatabase();


		GuildVo guildVo = dataSupport.getVo(guildId);
		Assert.assertNotNull(guildVo);
		guildVo.getGuildPo().insert();

		guildVo.getGuildPo().delete();
		dataSupport.syncToDatabase();
	}
}
