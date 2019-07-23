package org.qiunet.data.redis.util;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.core.support.redis.AbstractRedisUtil;
import org.qiunet.data.core.support.redis.RedisLock;

public class TestRedis {
	@Test
	public void testGetSet(){
		for (int i = 0; i < 10000; i++) {
			RedisDataUtil.jedis().set("qiu", "yang");
			String qiuVal = RedisDataUtil.jedis().get("qiu");
			Assert.assertEquals(qiuVal, "yang");
		}
	}
	@Test
	public void execCommands(){
		for (int i = 0; i < 10000; i++) {
			String str = RedisDataUtil.getInstance().execCommands(jedis -> {
				String qiuVal = jedis.get("qiu");
				jedis.expire("qiu", 100000);
				return qiuVal;
			});
			Assert.assertEquals("yang", str);
		}
	}

	@Test
	public void testLock(){
		try (RedisLock lock = RedisDataUtil.getInstance().redisLock("qiuyang")){
			if (lock.lock()){
				Assert.assertFalse(lock.lock());
			}
		}
	}
}
