package org.qiunet.utils.http;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 16/11/5 21:43.
 */

public class TestHttpRequest extends BaseTest{
	@Test
	public void testHttpsRequest() throws Exception {
		String url = "https://baidu.com";
		Map<String,String> params = new HashMap<>();
		params.put("wd", "qiunet");
		for (int i = 0 ; i < 2; i++){
			String ret = HttpRequest.post(url).withFormData(params).executor();
			System.out.println(ret);
			Assert.assertNotNull(ret);
		}
	}

	@Test
	public void testAsyncHttpsRequest() throws Exception {
		String url = "https://baidu.com";
		Map<String,String> params = new HashMap<>();
		params.put("wd", "qiunet");
		CountDownLatch latch = new CountDownLatch(2);
		for (int i = 0 ; i < latch.getCount(); i++){
			HttpRequest.get(url).params(params).asyncExecutor(
				(IHttpCallBack) (call, response) -> {
					String result = response.body().string();
					System.out.println(result);
					Assert.assertNotNull(result);
					latch.countDown();
				}
			);
		}
		latch.await();
	}

	@Test
	public void testHttpRequest() throws Exception {
		String url = "http://www.gameley.com";
		String ret = HttpRequest.get(url).executor();
		Assert.assertNotNull(ret);
	}
}
