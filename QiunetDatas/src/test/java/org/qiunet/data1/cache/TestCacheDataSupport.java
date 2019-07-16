package org.qiunet.data1.cache;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.support.CacheDataListSupport;
import org.qiunet.data1.support.CacheDataSupport;

import java.util.Map;

public class TestCacheDataSupport {
	private static CacheDataSupport<Long, GuildPo, GuildVo> dataSupport = new CacheDataSupport<>(GuildPo.class, GuildVo::new);

	private static CacheDataListSupport<Long, Long, GuildMemberPo, GuildMemberVo> dataListSupport = new CacheDataListSupport<>(GuildMemberPo.class, GuildMemberVo::new);
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

	@Test
	public void testEntityList(){
		GuildMemberPo memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		memberPo1.insert();

		GuildMemberPo memberPo2 = new GuildMemberPo();
		memberPo2.setGuildId(guildId);
		memberPo2.setMemberId(2);
		memberPo2.setJob(2);
		memberPo2.insert();

		dataListSupport.syncToDatabase();

		Map<Long, GuildMemberVo> voMap = dataListSupport.getVoMap(guildId);
		Assert.assertEquals(voMap.size(), 2);

		for (GuildMemberVo vo : voMap.values()) {
			Assert.assertTrue(vo.getGuildMemberPo().getJob() >= 1 && vo.getGuildMemberPo().getJob() <= 2);
			Assert.assertEquals(vo.getGuildMemberPo().getJob(), vo.getGuildMemberPo().getMemberId());
		}

		memberPo2.setJob(3);
		memberPo2.update();
		dataListSupport.syncToDatabase();

		dataListSupport.invalidate(guildId);

		voMap = dataListSupport.getVoMap(guildId);
		voMap.values().forEach(po -> {
			if (po.getGuildMemberPo().getMemberId() == 2){
				Assert.assertEquals(3, po.getGuildMemberPo().getJob());
			}
		});

		voMap.values().forEach(vo -> vo.getGuildMemberPo().delete());

		dataListSupport.syncToDatabase();

		voMap = dataListSupport.getVoMap(guildId);
		Assert.assertTrue(voMap.isEmpty());
	}
}
