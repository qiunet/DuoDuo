package org.qiunet.data1.redis;

import org.junit.Assert;
import org.junit.Test;

public class TestRedis {
	@Test
	public void testGetSet(){
		RedisDataUtil.returnJedis().set("qiu", "yang");

		String qiuVal = RedisDataUtil.returnJedis().get("qiu");
		Assert.assertEquals(qiuVal, "yang");
	}
	@Test
	public void execCommands(){
		String str = RedisDataUtil.getInstance().execCommands(jedis -> {
			String qiuVal = jedis.get("qiu");
			jedis.expire("qiu", 100000);
			return qiuVal;
		});
		Assert.assertEquals("yang", str);
	}
}
