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
		GuildVo guildVo = guildPo.insert();
		dataSupport.syncToDatabase();

		guildVo.getPo().setName("公会");
		guildVo.update();
//		guildVo.getPo().insert();
		dataSupport.syncToDatabase();


		guildVo = dataSupport.getVo(guildId);
		Assert.assertNotNull(guildVo);
//		guildVo.getPo().insert();

		guildVo.delete();
		dataSupport.syncToDatabase();

		guildVo = dataSupport.getVo(guildId);
		Assert.assertNull(guildVo);
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
		GuildMemberVo vo1 = memberPo1.insert();
		vo1.delete();
		vo1.delete();
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
		GuildMemberVo vo1 = memberPo1.insert();
		vo1.delete();

		memberPo1 = new GuildMemberPo();
		memberPo1.setGuildId(guildId);
		memberPo1.setMemberId(1);
		memberPo1.setJob(1);
		vo1 = memberPo1.insert();

		GuildMemberPo memberPo2 = new GuildMemberPo();
		memberPo2.setGuildId(guildId);
		memberPo2.setMemberId(2);
		memberPo2.setJob(2);
		GuildMemberVo vo2 = memberPo2.insert();

		dataListSupport.syncToDatabase();

		Map<Long, GuildMemberVo> voMap = dataListSupport.getVoMap(guildId);
		Assert.assertEquals(voMap.size(), 2);

		for (GuildMemberVo vo : voMap.values()) {
			Assert.assertTrue(vo.getPo().getJob() >= 1 && vo.getPo().getJob() <= 2);
			Assert.assertEquals(vo.getPo().getJob(), vo.getPo().getMemberId());
		}

		vo2.getPo().setJob(3);
		vo2.update();
		dataListSupport.syncToDatabase();

		dataListSupport.invalidate(guildId);

		voMap = dataListSupport.getVoMap(guildId);
		voMap.values().forEach(po -> {
			if (po.getPo().getMemberId() == 2){
				Assert.assertEquals(3, po.getPo().getJob());
			}
		});

		voMap.values().forEach(GuildMemberVo::delete);

		dataListSupport.syncToDatabase();

		voMap = dataListSupport.getVoMap(guildId);
		Assert.assertTrue(voMap.isEmpty());
	}
}
