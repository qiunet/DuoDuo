package org.qiunet.data1.redis;

import org.junit.Test;
import org.qiunet.data1.support.RedisDataSupport;

public class TestRedisDataSupport {
	private static RedisDataSupport<Long, VipDo, VipBo> dataSupport = new RedisDataSupport<>(RedisDataUtil.getInstance(), VipDo.class, VipBo::new);
	@Test
	public void testEntity(){

	}
}
