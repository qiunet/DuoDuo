package org.qiunet.data.redis.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.data.core.support.redis.RedisLock;
import org.qiunet.utils.scanner.ClassScanner;

public class TestRedis {
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner("org.qiunet.data");
	}

	@Test
	public void testGetSet(){
		RedisDataUtil.jedis().set("qiu1", "yang1");
		RedisDataUtil.jedis().set("qiu2", "yang2");
		String qiuVal = RedisDataUtil.jedis().get("qiu1");
		RedisDataUtil.jedis().mget("qiu1", "qiu2");
		Assert.assertEquals(qiuVal, "yang1");
	}
	@Test
	public void execCommands(){
		for (int i = 0; i < 1; i++) {
			String str = RedisDataUtil.getInstance().execCommands(jedis -> {
				String qiuVal = jedis.get("qiu1");
				jedis.expire("qiu1", 100000L);
				return qiuVal;
			});
			Assert.assertEquals("yang1", str);
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
