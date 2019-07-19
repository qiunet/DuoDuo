package org.qiunet.data1.redis;

import org.junit.Assert;
import org.junit.Test;

public class TestRedis {
	@Test
	public void testGetSet(){
		RedisDataUtil.getInstance().set("qiu", "yang");

		String qiuVal = RedisDataUtil.getInstance().get("qiu");
		Assert.assertEquals(qiuVal, "yang");
	}
}
