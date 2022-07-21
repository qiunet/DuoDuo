package org.qiunet.utils.test.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.test.base.BaseTest;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/22 12:05
 **/
public class TestNetUtil  extends BaseTest {

	/**
	 *
	 */
	@Test
	public void testInnerIp(){
		String ip = "192.168.1.200";
		Assertions.assertTrue(NetUtil.isInnerIp(ip));

		ip = "172.21.0.9";
		Assertions.assertTrue(NetUtil.isInnerIp(ip));

		ip = "123.196.125.13";
		Assertions.assertFalse(NetUtil.isInnerIp(ip));

		ip = "10.154.197.234";
		Assertions.assertTrue(NetUtil.isInnerIp(ip));

		ip = "127.0.0.1";
		Assertions.assertTrue(NetUtil.isInnerIp(ip));
		Assertions.assertTrue(NetUtil.isLocalIp(ip));
	}

	@Test
	public void testIsValidIp() {
		String ip = "192.168.1.255";
		Assertions.assertTrue(NetUtil.isValidIp4(ip));

		ip = "172.21.0.9";
		Assertions.assertTrue(NetUtil.isValidIp4(ip));

		ip = "123.196.125.13";
		Assertions.assertTrue(NetUtil.isValidIp4(ip));

		ip = "123.196.125.344";
		Assertions.assertFalse(NetUtil.isValidIp4(ip));

		ip = "https://baidu.com";
		Assertions.assertFalse(NetUtil.isValidIp4(ip));

		ip = "::1";
		Assertions.assertTrue(NetUtil.isValidIp6(ip));

		ip = "2408:4005:3e2:6d00:ca74:160b:7749:c6b8";
		Assertions.assertTrue(NetUtil.isValidIp6(ip));

		ip = "2408:4005:3g2:6d00:ca74:160b:7749:c6b8";
		Assertions.assertFalse(NetUtil.isValidIp6(ip));
	}

	/**
	 * 得到内网ip
	 */
	@Test
	public void testGetInnerIp(){
		logger.info("内网IP: "+NetUtil.getInnerIp());
	}

	/**
	 * 得到内网ip
	 */
	@Test
	public void testGetLocalHostName(){
		logger.info("内网HostName: "+NetUtil.getLocalHostName());
	}

	@Test
	public void testAllInnerIp(){
		for (String ip : NetUtil.getAllInnerIp()) {
			logger.info("Ip: "+ip);
		}
	}

	@Test
	public void testGetPublicIp4() {
		System.out.println(NetUtil.getPublicIp4());
	}
}
