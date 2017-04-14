package org.qiunet.utils.http;

import org.apache.http.client.config.RequestConfig;

/**
 * @author qiunet
 *         Created on 16/12/21 08:36.
 */
public class HttpUtils extends AbstractHttpUtil {
	private volatile static HttpUtils instance;

	private HttpUtils( HttpsClientPool httpsClientPool, RequestConfig requestConfig) {
		super(httpsClientPool , requestConfig );
	}

	/***
	 * 得到Https的实例
	 * @return
	 */
	public static HttpUtils getInstance() {
		if (instance == null) {
			synchronized (HttpUtils.class) {
				if (instance == null)
				{
					HttpsClientPool httpsClientPool = new HttpsClientPool(PoolProperties.getInstance());
					RequestConfig config = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
					instance = new HttpUtils(httpsClientPool, config);
				}
			}
		}
		return instance;
	}
}
