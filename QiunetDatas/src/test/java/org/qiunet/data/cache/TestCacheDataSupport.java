package org.qiunet.data.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.data.BaseTest;
import org.qiunet.data.support.CacheDataListSupport;
import org.qiunet.data.support.CacheDataSupport;

import java.util.Map;

public class TestCacheDataSupport extends BaseTest {
	private static final CacheDataSupport<Long, GuildDo, GuildBo> dataSupport = new CacheDataSupport<>(GuildDo.class, GuildBo::new);

	private static final CacheDataListSupport<Long, Long, GuildMemberDo, GuildMemberBo> dataListSupport = new CacheDataListSupport<>(GuildMemberDo.class, GuildMemberBo::new);
	private final long guildId = 100000;

	@Test
	public void testEntity(){
		GuildDo guildDo = new GuildDo();
		guildDo.setGuildId(guildId);
		guildDo.setName("公会1");
		guildDo.setLevel(10);
		GuildBo guildBo = dataSupport.insert(guildDo);
		dataSupport.syncToDatabase();

		guildBo.getDo().setName("公会");
		guildBo.update();
		dataSupport.syncToDatabase();


		guildBo = dataSupport.getBo(guildId);
		Assertions.assertNotNull(guildBo);

		guildBo.delete();
		dataSupport.syncToDatabase();

		guildBo = dataSupport.getBo(guildId);
		Assertions.assertNull(guildBo);
	}

	/**
	 * delete 两次
	 * 测试这个会抛出异常
	 */
	@Test
	public void testEntityException() {
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		GuildMemberBo bo1 = dataListSupport.insert(memberDo1);
		bo1.delete();
		Assertions.assertThrows(RuntimeException.class, bo1::delete);
	}

	@Test
	public void testCacheEntityHit(){
		long guildId = 1000000;
		GuildBo bo = dataSupport.getBo(guildId);
		Assertions.assertNull(bo);
		for (int i = 0; i < 10; i++) {
			bo = dataSupport.getBo(guildId);
			Assertions.assertNull(bo);
		}
		GuildDo guildDo = new GuildDo();
		guildDo.setGuildId(guildId);
		guildDo.setName("公会1");
		guildDo.setLevel(10);
		GuildBo bo1 = dataSupport.insert(guildDo);
		dataSupport.syncToDatabase();

		bo = dataSupport.getBo(guildId);
		Assertions.assertNotNull(bo);
		Assertions.assertEquals(bo, bo1);

		bo1.delete();
		dataSupport.syncToDatabase();
		bo = dataSupport.getBo(guildId);
		Assertions.assertNull(bo);
	}

	/**
	 * 两次insert
	 * 测试这个会抛出异常
	 */
	@Test
	public void testEntityInsertException() {
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		GuildMemberBo memberBo = dataListSupport.insert(memberDo1);
		memberBo.delete();

		Assertions.assertThrows(RuntimeException.class, () -> dataListSupport.insert(memberDo1));
	}


	@Test
	public void testEntityList(){
		GuildMemberDo memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		GuildMemberBo bo1 = dataListSupport.insert(memberDo1);
		bo1.delete();

		memberDo1 = new GuildMemberDo();
		memberDo1.setGuildId(guildId);
		memberDo1.setMemberId(1);
		memberDo1.setJob(1);
		dataListSupport.insert(memberDo1);

		GuildMemberDo memberDo2 = new GuildMemberDo();
		memberDo2.setGuildId(guildId);
		memberDo2.setMemberId(2);
		memberDo2.setJob(2);
		GuildMemberBo bo2 = dataListSupport.insert(memberDo2);

		dataListSupport.syncToDatabase();

		Map<Long, GuildMemberBo> boMap = dataListSupport.getBoMap(guildId);
		Assertions.assertEquals(boMap.size(), 2);

		for (GuildMemberBo bo : boMap.values()) {
			Assertions.assertTrue(bo.getDo().getJob() >= 1 && bo.getDo().getJob() <= 2);
			Assertions.assertEquals(bo.getDo().getJob(), bo.getDo().getMemberId());
		}

		bo2.getDo().setJob(3);
		bo2.update();
		dataListSupport.syncToDatabase();

		dataListSupport.invalidate(guildId);

		boMap = dataListSupport.getBoMap(guildId);
		boMap.values().forEach(po -> {
			if (po.getDo().getMemberId() == 2){
				Assertions.assertEquals(3, po.getDo().getJob());
			}
		});

		boMap.values().forEach(GuildMemberBo::delete);

		dataListSupport.syncToDatabase();

		boMap = dataListSupport.getBoMap(guildId);
		Assertions.assertTrue(boMap.isEmpty());
	}
}
