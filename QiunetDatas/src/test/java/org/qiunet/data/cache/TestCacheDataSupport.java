package org.qiunet.data.cache;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.data.support.CacheDataListSupport;
import org.qiunet.data.support.CacheDataSupport;
import org.qiunet.utils.scanner.ClassScanner;

import java.util.Map;

public class TestCacheDataSupport {
	private static CacheDataSupport<Long, GuildDo, GuildBo> dataSupport = new CacheDataSupport<>(GuildDo.class, GuildBo::new);

	private static CacheDataListSupport<Long, Long, GuildMemberDo, GuildMemberBo> dataListSupport = new CacheDataListSupport<>(GuildMemberDo.class, GuildMemberBo::new);
	private long guildId = 100000;

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void testEntity(){
		GuildDo guildDo = new GuildDo();
		guildDo.setGuildId(guildId);
		guildDo.setName("公会1");
		guildDo.setLevel(10);
		GuildBo guildBo = guildDo.insert();
		dataSupport.syncToDatabase();

		guildBo.getDo().setName("公会");
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
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		GuildMemberBo bo1 = memberDo1.insert();
		bo1.delete();
		bo1.delete();
	}

	@Test
	public void testCacheEntityHit(){
		long guildId = 1000000;
		GuildBo bo = dataSupport.getBo(guildId);
		Assert.assertNull(bo);
		for (int i = 0; i < 10; i++) {
			bo = dataSupport.getBo(guildId);
			Assert.assertNull(bo);
		}
		GuildDo guildDo = new GuildDo();
		guildDo.setGuildId(guildId);
		guildDo.setName("公会1");
		guildDo.setLevel(10);
		GuildBo bo1 = guildDo.insert();
		dataSupport.syncToDatabase();

		bo = dataSupport.getBo(guildId);
		Assert.assertNotNull(bo);
		Assert.assertEquals(bo, bo1);

		bo1.delete();
		dataSupport.syncToDatabase();
		bo = dataSupport.getBo(guildId);
		Assert.assertNull(bo);
	}

	/**
	 * 两次insert
	 * 测试这个会抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void testEntityInsertException() {
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		memberDo1.insert();
		memberDo1.delete();
		memberDo1.insert();
	}
	/**
	 * 没有insert 就update
	 * 测试这个会抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void testEntityUpdateException() {
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		memberDo1.update();
	}

	@Test
	public void testEntityList(){
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		GuildMemberBo bo1 = memberDo1.insert();
		bo1.delete();

		memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		bo1 = memberDo1.insert();

		GuildMemberDo memberDo2 = new GuildMemberDo();
		memberDo2.setGuildId(guildId);
		memberDo2.setMemberId(2);
		memberDo2.setJob(2);
		GuildMemberBo bo2 = memberDo2.insert();

		dataListSupport.syncToDatabase();

		Map<Long, GuildMemberBo> boMap = dataListSupport.getBoMap(guildId);
		Assert.assertEquals(boMap.size(), 2);

		for (GuildMemberBo bo : boMap.values()) {
			Assert.assertTrue(bo.getDo().getJob() >= 1 && bo.getDo().getJob() <= 2);
			Assert.assertEquals(bo.getDo().getJob(), bo.getDo().getMemberId());
		}

		bo2.getDo().setJob(3);
		bo2.update();
		dataListSupport.syncToDatabase();

		dataListSupport.invalidate(guildId);

		boMap = dataListSupport.getBoMap(guildId);
		boMap.values().forEach(po -> {
			if (po.getDo().getMemberId() == 2){
				Assert.assertEquals(3, po.getDo().getJob());
			}
		});

		boMap.values().forEach(GuildMemberBo::delete);

		dataListSupport.syncToDatabase();

		boMap = dataListSupport.getBoMap(guildId);
		Assert.assertTrue(boMap.isEmpty());
	}
}
