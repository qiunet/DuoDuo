package org.qiunet.data.redis;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiunet
 *         Created on 17/1/5 08:39.
 */
public class TestRedisData {
	@BeforeClass
	public static void beforeClass(){
		RedisDataUtil.getInstance();
	}
	@Test
	public void testTtl(){
		RedisDataUtil.returnJedis().setex("qiunet",40, "123");
		long ttl = RedisDataUtil.returnJedis().ttl("qiunet");
		Assert.assertTrue(ttl == 40);

		RedisDataUtil.returnJedis().sadd("qiunet111", "1", "2", "3");
	}

	@Test
	public void testSetGetUidObject(){
		String key = "pP#1000#a";
		PlayerPo playerPo = new PlayerPo();
		playerPo.setUid(1000);
		playerPo.setLevel(10);
		playerPo.setExp(2000);
		RedisDataUtil.getInstance().setObjectToHash(key, playerPo);

		PlayerPo playerPo1 = RedisDataUtil.getInstance().getObjectFromHash(key, PlayerPo.class);
		Assert.assertTrue(playerPo1.getExp() == playerPo.getExp());

		PlayerCopyPo playerCopyPo = RedisDataUtil.getInstance().getObjectFromHash(key, PlayerCopyPo.class);
		Assert.assertNotNull(playerCopyPo);

	}
	@Test
	public void testSetGetUidList(){
		String key = "el#1000#a";
		List<EquipPo> equipList = new ArrayList<EquipPo>();
		for (int i = 0 ; i < 2; i++) {
			EquipPo equipPo = new EquipPo();
			equipPo.setExp(10 + i);
			equipPo.setUid(1000);
			equipPo.setId(10000 + i);
			equipPo.setLevel(20 + i);
			equipList.add(equipPo);
		}

		RedisDataUtil.getInstance().setListToHash(key , equipList);

		List<EquipPo> equipList2 = RedisDataUtil.getInstance().getListFromHash(key, EquipPo.class);
		Assert.assertNotNull(equipList2);
		for (EquipPo po : equipList2) {
			if (po.getId() == 10000) {
				Assert.assertTrue(po.getLevel() == 20);
			}
		}

		List<EquipCopyPo> equipList3 = RedisDataUtil.getInstance().getListFromHash(key, EquipCopyPo.class);
		Assert.assertNotNull(equipList3);
	}
	@Test
	public void testGetSetFriendPo(){
		String key = "friend#1000";
		FriendPo friendPo = new FriendPo();
		friendPo.setUid(1000);
		friendPo.setFriend_descs("qiunet,1;qiunet1,0");
		RedisDataUtil.getInstance().setObjectToHash(key , friendPo);

		FriendPo friendPo1 = RedisDataUtil.getInstance().getObjectFromHash(key , FriendPo.class);
		Assert.assertEquals("qiunet,1;qiunet1,0", friendPo1.getFriend_descs());

		friendPo.setFriend_descs("qiunet3");

		RedisDataUtil.getInstance().setObjectToHash(key, friendPo);

		friendPo1 = RedisDataUtil.getInstance().getObjectFromHash(key , FriendPo.class);
		Assert.assertEquals("qiunet3", friendPo1.getFriend_descs());
	}
	@Test
	public void testGetSetGuildPo(){
		String key = "gd#1";
		QunxiuPo qunxiuPo = new QunxiuPo();
		qunxiuPo.setId(1);
		qunxiuPo.setName("生死门");
		qunxiuPo.setMaster(1000);
		qunxiuPo.setLevel(10);

		RedisDataUtil.getInstance().setObjectToHash(key , qunxiuPo);

		QunxiuPo guildPo1 = RedisDataUtil.getInstance().getObjectFromHash(key, QunxiuPo.class);
		Assert.assertEquals(guildPo1.getName(), qunxiuPo.getName());

		Assert.assertTrue(RedisDataUtil.returnJedis().exists(key));

		RedisDataUtil.getInstance().returnJedis().expire(key , 0);
		Assert.assertFalse(RedisDataUtil.returnJedis().exists(key));
	}

	@Test public void testMget(){
		List<String> keys = new ArrayList<>();
		List<String> ret = RedisDataUtil.getInstance().mget(keys);
		Assert.assertTrue(ret.isEmpty());
	}
}
