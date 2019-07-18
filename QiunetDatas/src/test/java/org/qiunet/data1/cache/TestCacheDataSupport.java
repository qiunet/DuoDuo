package org.qiunet.data1.cache;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.support.CacheDataListSupport;
import org.qiunet.data1.support.CacheDataSupport;

import java.util.Map;

public class TestCacheDataSupport {
	private static CacheDataSupport<Long, GuildPo, GuildBo> dataSupport = new CacheDataSupport<>(GuildPo.class, GuildBo::new);

	private static CacheDataListSupport<Long, Long, GuildMemberPo, GuildMemberBo> dataListSupport = new CacheDataListSupport<>(GuildMemberPo.class, GuildMemberBo::new);
	private long guildId = 100000;

	@Test
	public void testEntity(){
		GuildPo guildPo = new GuildPo();
		guildPo.setGuildId(guildId);
		guildPo.setName("公会1");
		guildPo.setLevel(10);
		GuildBo guildBo = guildPo.insert();
		dataSupport.syncToDatabase();

		guildBo.getPo().setName("公会");
		guildBo.update();
		dataSupport.syncToDatabase();


		guildBo = dataSupport.getBo(guildId);
		Assert.assertNotNull(guildBo);

		guildBo.delete();
		dataSupport.syncToDatabase();

		guildBo = dataSupport.getBo(guildId);
		Assert.assertNull(guildBo);
	}

	/**
	 * delete 两次
	 * 测试这个会抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void testEntityException() {
		GuildMemberPo memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		GuildMemberBo bo1 = memberPo1.insert();
		bo1.delete();
		bo1.delete();
	}

	/**
	 * 两次insert
	 * 测试这个会抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void testEntityInsertException() {
		GuildMemberPo memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		memberPo1.insert();
		memberPo1.delete();
		memberPo1.insert();
	}
	/**
	 * 没有insert 就update
	 * 测试这个会抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void testEntityUpdateException() {
		GuildMemberPo memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		memberPo1.update();
	}

	@Test
	public void testEntityList(){
		GuildMemberPo memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		GuildMemberBo bo1 = memberPo1.insert();
		bo1.delete();

		memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		bo1 = memberPo1.insert();

		GuildMemberPo memberPo2 = new GuildMemberPo();
		memberPo2.setGuildId(guildId);
		memberPo2.setMemberId(2);
		memberPo2.setJob(2);
		GuildMemberBo bo2 = memberPo2.insert();

		dataListSupport.syncToDatabase();

		Map<Long, GuildMemberBo> boMap = dataListSupport.getBoMap(guildId);
		Assert.assertEquals(boMap.size(), 2);

		for (GuildMemberBo bo : boMap.values()) {
			Assert.assertTrue(bo.getPo().getJob() >= 1 && bo.getPo().getJob() <= 2);
			Assert.assertEquals(bo.getPo().getJob(), bo.getPo().getMemberId());
		}

		bo2.getPo().setJob(3);
		bo2.update();
		dataListSupport.syncToDatabase();

		dataListSupport.invalidate(guildId);

		boMap = dataListSupport.getBoMap(guildId);
		boMap.values().forEach(po -> {
			if (po.getPo().getMemberId() == 2){
				Assert.assertEquals(3, po.getPo().getJob());
			}
		});

		boMap.values().forEach(GuildMemberBo::delete);

		dataListSupport.syncToDatabase();

		boMap = dataListSupport.getBoMap(guildId);
		Assert.assertTrue(boMap.isEmpty());
	}
}
