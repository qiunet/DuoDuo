package org.qiunet.data.redis;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.core.support.db.DbSourceDatabaseSupport;
import org.qiunet.data.core.support.db.Table;
import org.qiunet.data.redis.util.RedisDataUtil;
import org.qiunet.data.support.RedisDataSupport;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.thread.ThreadContextData;

public class TestRedisDataSupport {
	private static RedisDataSupport<Long, VipDo, VipBo> dataSupport;

	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
		dataSupport = new RedisDataSupport<>(RedisDataUtil.getInstance(), VipDo.class, VipBo::new);
	}

	public void expire(long uid) {
		String redisKey = "VipDo#"+uid;
		RedisDataUtil.jedis().expire(redisKey, 0L);
		ThreadContextData.removeKey(redisKey);
	}
	private final long uid = 10000;
	@Test
	public void testEntity(){
		VipDo vipDo = new VipDo();
		vipDo.setUid(uid);
		vipDo.setLevel(10);
		vipDo.setExp(1000);

		vipDo.insert();
		dataSupport.syncToDatabase();
		this.expire(uid);

		VipBo bo = dataSupport.getBo(uid);
		Assert.assertEquals(bo.getDo().getUid(), uid);
		Assert.assertEquals(bo.getDo().getLevel(), 10);
		Assert.assertEquals(bo.getDo().getExp(), 1000);

		bo.getDo().setExp(100);
		bo.update();
		dataSupport.syncToDatabase();

		this.expire(uid);
		bo = dataSupport.getBo(uid);
		Assert.assertEquals(bo.getDo().getExp(), 100);

		bo.delete();
		dataSupport.syncToDatabase();
		bo = dataSupport.getBo(uid);
		Assert.assertNull(bo);
	}
	@Test
	public void testDuplicateInsert(){
		VipDo vipDo = new VipDo();
		vipDo.setUid(uid);
		vipDo.setLevel(10);
		vipDo.setExp(1000);

		vipDo.insert();
		dataSupport.syncToDatabase();

		vipDo.delete();
		dataSupport.syncToDatabase();
		this.expire(uid);

		VipBo vipBo = dataSupport.getBo(uid);
		Assert.assertNull(vipBo);
	}

	/***
	 * 测试异步删除抛异常的情况
	 *
	 * 操作步骤:
	 * {@link org.qiunet.data.core.support.db.DbSourceDatabaseSupport#delete(String, Object)} 取消注释
	 * 执行测试方法
	 */
	@Test
	public void testDeleteException (){
		VipDo vipDo = new VipDo();
		vipDo.setUid(uid);
		vipDo.setLevel(10);
		vipDo.setExp(1000);
		vipDo.insert();
		dataSupport.syncToDatabase();

		Table table = vipDo.getClass().getAnnotation(Table.class);
		DbParamMap map = DbParamMap.create(table, vipDo.keyFieldName(), vipDo.key());
		DbSourceDatabaseSupport.getInstance(table.dbSource()).insert("deleteVipDo", map);

		vipDo.delete();
		dataSupport.syncToDatabase();

		vipDo = new VipDo();
		vipDo.setUid(uid);
		vipDo.setLevel(8);
		vipDo.setExp(100);
		vipDo.insert();
		dataSupport.syncToDatabase();

		vipDo.delete();
		dataSupport.syncToDatabase();

		this.expire(uid);

		VipBo vipBo = dataSupport.getBo(uid);
		Assert.assertNull(vipBo);

	}

	@Test
	public void testRedisHit(){
		long uid = 100000000;

		VipBo bo = dataSupport.getBo(uid);
		Assert.assertNull(bo);

		for (int i = 0; i < 3; i++) {
			bo = dataSupport.getBo(uid);
			Assert.assertNull(bo);
		}
	}
}
