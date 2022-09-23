package org.qiunet.utils.test.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.test.base.BaseTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 16/11/5 21:43.
 */

public class TestHttpRequest extends BaseTest{
	@Test
	@Disabled("Do it manual!")
	public void testHttpsRequest() throws Exception {
		String url = "https://baidu.com";
		Map<String,String> params = new HashMap<>();
		params.put("wd", "qiunet");
		for (int i = 0 ; i < 2; i++){
			String ret = HttpRequest.post(url).withFormData(params).executor();
			System.out.println(ret);
			Assertions.assertNotNull(ret);
		}
	}

	@Test
	@Disabled("Do it manual!")
	public void testAsyncHttpsRequest() throws Exception {
		String url = "https://www.qq.com";
		Map<String,String> params = new HashMap<>();
		params.put("wd", "qiunet");
		CountDownLatch latch = new CountDownLatch(2);
		for (int i = 0 ; i < latch.getCount(); i++){
			HttpRequest.get(url).params(params).asyncExecutor(
				(response) -> {
					String result = response.body();
					System.out.println(result);
					Assertions.assertNotNull(result);
					latch.countDown();
				}
			);
		}
		latch.await();
	}

	@Test
	@Disabled("Do it manual!")
	public void testHttpRequest() throws Exception {
		String url = "http://www.gameley.com";
		String ret = HttpRequest.get(url).executor();
		Assertions.assertNotNull(ret);
	}
}
