package org.qiunet.data1.redis;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data1.core.select.DbParamMap;
import org.qiunet.data1.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data1.redis.util.RedisDataUtil;
import org.qiunet.data1.support.RedisDataSupport;

public class TestRedisDataSupport {
	private static RedisDataSupport<Long, VipDo, VipBo> dataSupport = new RedisDataSupport<>(RedisDataUtil.getInstance(), VipDo.class, VipBo::new);

	private long uid = 10000;
	@Test
	public void testEntity(){
		VipDo vipDo = new VipDo();
		vipDo.setUid(uid);
		vipDo.setLevel(10);
		vipDo.setExp(1000);

		vipDo.insert();
		dataSupport.syncToDatabase();
		dataSupport.expire(uid);

		VipBo bo = dataSupport.getBo(uid);
		Assert.assertEquals(bo.getDo().getUid(), uid);
		Assert.assertEquals(bo.getDo().getLevel(), 10);
		Assert.assertEquals(bo.getDo().getExp(), 1000);

		bo.getDo().setExp(100);
		bo.update();
		dataSupport.syncToDatabase();

		dataSupport.expire(uid);
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

		MoreDbSourceDatabaseSupport.getInstance(vipDo.getDbSourceKey()).insert("insertVipDo", vipDo);

		vipDo.insert();
		dataSupport.syncToDatabase();

		vipDo.delete();
		dataSupport.syncToDatabase();
		dataSupport.expire(uid);

		VipBo vipBo = dataSupport.getBo(uid);
		Assert.assertNull(vipBo);
	}

	/***
	 * 测试异步删除抛异常的情况
	 *
	 * 操作步骤:
	 * {@link org.qiunet.data1.core.support.db.BaseDatabaseSupport#delete(String, Object)} 取消注释
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

		DbParamMap map = DbParamMap.create().put(vipDo.keyFieldName(), vipDo.key())
			.put("dbName", vipDo.getDbName());
		MoreDbSourceDatabaseSupport.getInstance(vipDo.getDbSourceKey()).insert("deleteVipDo", map);

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

		dataSupport.expire(uid);

		VipBo vipBo = dataSupport.getBo(uid);
		Assert.assertNull(vipBo);

	}
}
