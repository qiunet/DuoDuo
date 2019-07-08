package org.qiunet.utils.http;

import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 16/11/5 21:43.
 */

public class TestHttpUtil extends BaseTest{
	@Test
	public void testHttpsRequest(){
		String url = "https://baidu.com";
		Map<String,Object> params = new HashMap<>();
		params.put("wd", "qiunet");
		for (int i = 0 ; i < 2; i++){
			String ret = HttpUtil.httpRequest(url, HttpMethodEnum.GET , params, new HashMap<>());
			System.out.println(ret);
			Assert.assertNotNull(ret);
		}
	}

	@Test
	public void testAsyncHttpsRequest() throws InterruptedException {
		String url = "https://baidu.com";
		Map<String,Object> params = new HashMap<>();
		params.put("wd", "qiunet");
		CountDownLatch latch = new CountDownLatch(2);
		for (int i = 0 ; i < latch.getCount(); i++){
			AsyncHttpUtil.post(url, params, new Callback() {
				@Override
				public void completed(String result) {
					System.out.println(result);
					Assert.assertNotNull(result);
					latch.countDown();
				}

				@Override
				public void failed(Exception ex) {
					ex.printStackTrace();
				}
			});
		}
		latch.await();
	}

	@Test
	public void testHttpRequest(){
		String url = "http://www.gameley.com";
		String ret = HttpUtil.httpRequest(url, HttpMethodEnum.GET , new HashMap<>(), new HashMap<>());
		Assert.assertNotNull(ret);
	}
}
