package org.qiunet.utils.http;

import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;
import org.qiunet.utils.enums.HttpMethodEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/11/5 21:43.
 */

public class TestHttpUtil extends BaseTest{
	@Test
	public void testHttpsRequest(){
		String url = "https://baidu.com";
		Map<String,Object> params = new HashMap();
		params.put("wd", "qiunet");
		for (int i = 0 ; i < 5; i++){
			String ret = HttpUtils.getInstance().httpRequest(url, HttpMethodEnums.GET , params, new HashMap<String,Object>());
			Assert.assertNotNull(ret);
			logger.info(i+"==================="+ret);
		}
	}
	@Test
	public void testHttpRequest(){
		String url = "http://www.i8wan.com";
		String ret = HttpUtils.getInstance().httpRequest(url, HttpMethodEnums.GET , new HashMap<String,Object>(), new HashMap<String,Object>());
		Assert.assertNotNull(ret);
	}
}
