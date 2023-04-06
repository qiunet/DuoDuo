package org.qiunet.data.redis.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.data.core.support.redis.RedisLock;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

public class TestRedis {
	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner("org.qiunet.data");
	}

	@Test
	public void testGetSet(){
		RedisDataUtil.jedis().set("qiu1", "yang1");
		RedisDataUtil.jedis().set("qiu2", "yang2");
		String qiuVal = RedisDataUtil.jedis().get("qiu1");
		RedisDataUtil.jedis().mget("qiu1", "qiu2");
		Assertions.assertEquals(qiuVal, "yang1");
	}
	@Test
	public void execCommands(){
		for (int i = 0; i < 1; i++) {
			String str = RedisDataUtil.getInstance().execCommands(jedis -> {
				String qiuVal = jedis.get("qiu1");
				jedis.expire("qiu1", 100000L);
				return qiuVal;
			});
			Assertions.assertEquals("yang1", str);
		}
	}

	/**
	 * 测试是否错误. 不能获取
	 */
	@Test
	public void testLock1() {
		try (RedisLock lock = RedisDataUtil.getInstance().redisLock("qiuyang")){
			if (lock.tryLock()){
				Assertions.assertFalse(lock.tryLock());
			}
		}
	}

	/**
	 * 测试锁是否正常
	 */
	@Test
	public void testLock2() {
		try (RedisLock lock = RedisDataUtil.getInstance().redisLock("qiuyang")){
			if (lock.tryLock()){
				System.out.println("====");
			}
		}
	}
}
