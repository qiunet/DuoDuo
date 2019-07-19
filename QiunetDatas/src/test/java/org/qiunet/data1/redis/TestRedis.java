package org.qiunet.data1.redis;

import org.junit.Assert;
import org.junit.Test;

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
}
