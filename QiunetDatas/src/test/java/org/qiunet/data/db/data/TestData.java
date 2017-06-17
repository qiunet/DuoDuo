package org.qiunet.data.db.data;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qiunet.data.core.support.EntityDataSupport;
import org.qiunet.data.core.support.EntityListDataSupport;
import org.qiunet.data.core.support.PlatformEntityDataSupport;
import org.qiunet.data.core.support.PlatformEntityListDataSupport;
import org.qiunet.data.core.support.update.UpdateFields;
import org.qiunet.data.db.data.equip.EquipEntityInfo;
import org.qiunet.data.db.data.equip.EquipVo;
import org.qiunet.data.db.data.friend.FriendEntityInfo;
import org.qiunet.data.db.data.friend.FriendVo;
import org.qiunet.data.db.data.global.GlobalTableEntityInfo;
import org.qiunet.data.db.data.login.LoginEntityInfo;
import org.qiunet.data.db.data.login.LoginPo;
import org.qiunet.data.db.data.player.PlayerEntityInfo;
import org.qiunet.data.db.data.player.PlayerVo;
import org.qiunet.data.db.data.qunxiu.QunxiuEntityInfo;
import org.qiunet.data.db.data.sysmsg.SysmsgEntityInfo;
import org.qiunet.data.db.data.sysmsg.SysmsgVo;
import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.entity.*;
import org.qiunet.data.redis.key.RedisKey;
import org.qiunet.utils.threadLocal.ThreadContextData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/2/12 11:16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:bean/applicationContext*"})
public class TestData {

	@Test
	public void testEntityData(){
		FriendEntityInfo entityInfo = new FriendEntityInfo();
		EntityDataSupport<FriendPo , FriendVo> dataSupport = new EntityDataSupport<FriendPo , FriendVo>(entityInfo);

		int uid = 10000;
		FriendPo friendPo = new FriendPo();
		friendPo.setFriend_descs("qiuyang");
		friendPo.setUid(uid);
		int ret = dataSupport.insertPo(friendPo);
		Assert.assertTrue(ret == 1);
		ThreadContextData.removeAll();

		FriendVo friendVo = dataSupport.getVo(uid);
		Assert.assertTrue(friendVo != null && friendVo.getFriendPo().getFriend_descs().equals("qiuyang"));

		friendVo.getFriendPo().setFriend_descs("desc");
		dataSupport.updatePo(friendVo.getFriendPo());
		dataSupport.updateRedisDataToDatabase();
		ThreadContextData.removeAll();

		friendVo = dataSupport.getVo(uid);
		Assert.assertTrue(friendVo != null && friendVo.getFriendPo().getFriend_descs().equals("desc"));

		dataSupport.deletePo(friendPo);

		friendVo = dataSupport.getVo(uid);
		Assert.assertNull(friendVo);
	}

	@Test
	public void testPlatformEntityData(){
		PlayerEntityInfo entityInfo = new PlayerEntityInfo();
		PlatformEntityDataSupport<PlayerPo, PlayerVo> dataSupport = new PlatformEntityDataSupport<PlayerPo, PlayerVo>(entityInfo);

		int uid = 1101;
		PlatformType platform = PlatformType.IOS;
		PlayerPo playerPo = new PlayerPo();
		playerPo.setUid(uid);
		playerPo.setExp(100);
		playerPo.setLevel(10);
		playerPo.setPlatform(platform);
		int ret = dataSupport.insertPo(playerPo);
		Assert.assertTrue(ret == 1);
		ThreadContextData.removeAll();

		PlayerVo playerVo = dataSupport.getVo(uid, platform);
		playerPo = playerVo.getPlayerPo();
		Assert.assertTrue(playerVo != null && playerVo.getPlayerPo().getExp() == 100);
		playerPo.setExp(1000);
		dataSupport.updatePo(playerPo);
		dataSupport.updateRedisDataToDatabase();
		ThreadContextData.removeAll();

		playerVo = dataSupport.getVo(uid, platform);
		Assert.assertTrue(playerVo != null && playerVo.getPlayerPo().getExp() == 1000);

		dataSupport.deletePo(playerVo.getPlayerPo());
		playerVo = dataSupport.getVo(uid, platform);
		Assert.assertNull(playerVo);
	}

	@Test
	public void testEntityListData(){
		SysmsgEntityInfo entityListInfo = new SysmsgEntityInfo();
		EntityListDataSupport dataSupport = new EntityListDataSupport(entityListInfo);

		int uid = 2000;
		SysmsgPo sysMsgPo1 = new SysmsgPo();
		sysMsgPo1.setUid(uid);
		sysMsgPo1.setMsg("msg1");
		sysMsgPo1.setParam("param1");
		int ret = dataSupport.insertPo(sysMsgPo1);
		Assert.assertTrue(ret == 1);

		SysmsgPo sysMsgPo2 = new SysmsgPo();
		sysMsgPo2.setUid(uid);
		sysMsgPo2.setMsg("msg2");
		sysMsgPo2.setParam("param2");
		ret = dataSupport.insertPo(sysMsgPo2);
		Assert.assertTrue(ret == 1);

		Map<Integer, SysmsgVo> map = dataSupport.getVoMap(uid);
		Assert.assertTrue(map != null && map.size() == 2);

		for (SysmsgVo vo : map.values()) {
			vo.getSysMsgPo().setParam("param");
			dataSupport.updatePo(vo.getSysMsgPo());
		}
		dataSupport.updateRedisDataToDatabase();
		ThreadContextData.removeAll();

		map = dataSupport.getVoMap(uid);
		Iterator<Map.Entry<Integer,SysmsgVo>> it = map.entrySet().iterator();
		while(it.hasNext()){
			SysmsgVo vo = it.next().getValue();
			Assert.assertEquals("param", vo.getSysMsgPo().getParam());
			it.remove();

			dataSupport.deletePo(vo.getSysMsgPo());
		}

		ThreadContextData.removeAll();
		map = dataSupport.getVoMap(uid);
		Assert.assertTrue(map == null || map.isEmpty());
	}

	@Test
	public void testPlatformEntityListData(){
		int uid = 10001;
		PlatformType platformType = PlatformType.ANDROID;

		EquipEntityInfo equipEntityInfo = new EquipEntityInfo();
		PlatformEntityListDataSupport<EquipPo, EquipVo> dataSupport = new PlatformEntityListDataSupport(equipEntityInfo);
		EquipPo equipPo1 = new EquipPo();
		equipPo1.setPlatform(platformType);
		equipPo1.setUid(uid);
		equipPo1.setId(10004);
		equipPo1.setExp(100);
		equipPo1.setLevel(10);
		int ret1 = dataSupport.insertPo(equipPo1);

		ThreadContextData.removeAll();
		EquipPo equipPo2 = new EquipPo();
		equipPo2.setPlatform(platformType);
		equipPo2.setUid(uid);
		equipPo2.setId(20004);
		equipPo2.setExp(200);
		equipPo2.setLevel(20);
		int ret2 = dataSupport.insertPo(equipPo2);
		Assert.assertTrue(ret2 == 1);

		Map<Integer, EquipVo> map = dataSupport.getVoMap(uid, platformType);
		Assert.assertTrue(map != null && map.size() == 2);

		for (EquipVo vo : map.values()) {
			vo.getEquipPo().setExp(vo.getEquipPo().getExp() + 1);
			dataSupport.updatePo(vo.getEquipPo());
		}
		dataSupport.updateRedisDataToDatabase();
		ThreadContextData.removeAll();

		map = dataSupport.getVoMap(uid, platformType);
		Iterator<Map.Entry<Integer,EquipVo>> it = map.entrySet().iterator();
		while(it.hasNext()){
			EquipVo vo = it.next().getValue();
			Assert.assertTrue(vo.getEquipPo().getExp() == 101 || vo.getEquipPo().getExp() == 201);
			it.remove();

			dataSupport.deletePo(vo.getEquipPo());
		}

		ThreadContextData.removeAll();
		map = dataSupport.getVoMap(uid, platformType);
		Assert.assertTrue(map == null || map.isEmpty());
	}
	@Test
	public void testLogin(){
		String openid = "qiunet12345";
		LoginEntityInfo entityInfo = new LoginEntityInfo();
		EntityDataSupport<LoginPo, LoginPo> dataSupport = new EntityDataSupport<LoginPo, LoginPo>(entityInfo);

		LoginPo loginPo = new LoginPo();
		loginPo.setOpenid(openid);
		loginPo.setUid(1000);
		loginPo.setToken("qiuyang");
		dataSupport.insertPo(loginPo);

		LoginPo loginPo1 = dataSupport.getVo(openid);
		Assert.assertNotNull(loginPo1);
		Assert.assertTrue(loginPo1.getUid() == 1000 && "qiuyang".equals(loginPo1.getToken()));

		loginPo1.setToken("qiuyang1");
		dataSupport.updatePo(loginPo1);
		dataSupport.updateRedisDataToDatabase();
		ThreadContextData.removeAll();

		LoginPo loginPo2 = dataSupport.getVo(openid);
		Assert.assertTrue(loginPo2.getUid() == 1000 && "qiuyang1".equals(loginPo2.getToken()));

		dataSupport.deletePo(loginPo2);
		LoginPo loginPo3 = dataSupport.getVo(openid);
		Assert.assertNull(loginPo3);
	}
	@Test
	public void testQunxiu(){
		QunxiuEntityInfo entityInfo = new QunxiuEntityInfo();
		EntityDataSupport<QunxiuPo, QunxiuPo> dataSupport = new EntityDataSupport(entityInfo);

		QunxiuPo qunxiuPo = new QunxiuPo();
		qunxiuPo.setLevel(10);
		qunxiuPo.setName("生死门");
		qunxiuPo.setId(1001);
		qunxiuPo.setMaster(1000);
		int ret = dataSupport.insertPo(qunxiuPo);
		Assert.assertTrue(ret == 1);
		dataSupport.expireCache(qunxiuPo.getId());

		qunxiuPo = dataSupport.getVo(qunxiuPo.getId());
		qunxiuPo.setMaster(1200);
		qunxiuPo.setLevel(20);
		dataSupport.updatePo(qunxiuPo);
		dataSupport.updateRedisDataToDatabase();
		dataSupport.expireCache(qunxiuPo.getId());

		qunxiuPo = dataSupport.getVo(qunxiuPo.getId());
		Assert.assertTrue(qunxiuPo.getLevel() == 20 && qunxiuPo.getMaster() == 1200);

		ThreadContextData.removeAll();

		qunxiuPo.setName("temp");
		qunxiuPo.setLevel(10);
		dataSupport.updateWithFields(qunxiuPo, UpdateFields.newBuild().append(QunxiuPo.FIELD_LEVEL));

		ThreadContextData.removeAll();

		qunxiuPo = dataSupport.getVo(qunxiuPo.getId());
		Assert.assertTrue(qunxiuPo.getLevel() == 10 && ! "temp".equals(qunxiuPo.getName()));
		dataSupport.expireCache(qunxiuPo.getId());

		dataSupport.deletePo(qunxiuPo);
		qunxiuPo = dataSupport.getVo(qunxiuPo.getId());
		Assert.assertNull(qunxiuPo);
	}
	@Test
	public void testGlobalTable(){
		GlobalTableEntityInfo entityInfo = new GlobalTableEntityInfo();
		EntityDataSupport<GlobalTablePo, GlobalTablePo> dataSupport = new EntityDataSupport<GlobalTablePo, GlobalTablePo>(entityInfo);

		GlobalTablePo globalTablePo = new GlobalTablePo();
		globalTablePo.setName("qiunet");
		int ret = dataSupport.insertPo(globalTablePo);
		Assert.assertTrue(ret == 1);
		int id = globalTablePo.getId();

		dataSupport.expireCache(globalTablePo.getId());

		globalTablePo = dataSupport.getVo(id);
		Assert.assertEquals("qiunet", globalTablePo.getName());
		globalTablePo.setName("qiuyang");
		dataSupport.updatePo(globalTablePo);
		dataSupport.updateRedisDataToDatabase();
		dataSupport.expireCache(id);

		globalTablePo = dataSupport.getVo(id);
		Assert.assertEquals("qiuyang", globalTablePo.getName());
		dataSupport.deletePo(globalTablePo);

		globalTablePo = dataSupport.getVo(id);
		Assert.assertNull(globalTablePo);
	}
}
