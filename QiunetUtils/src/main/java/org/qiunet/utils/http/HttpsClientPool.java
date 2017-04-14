package org.qiunet.utils.http;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.pool.BasicPool;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author qiunet
 *         Created on 16/12/21 08:12.
 */
public class HttpsClientPool extends BasicPool<HttpClient> {
	private HttpClientBuilder builder;
	public HttpsClientPool(IKeyValueData keyValueData){
		super(keyValueData);

		SSLContextBuilder sslContextBuilder=new SSLContextBuilder();
		SSLConnectionSocketFactory sslsf = null;
		try {
			sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			});
			sslsf=new SSLConnectionSocketFactory(sslContextBuilder.build());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		this.builder = HttpClientBuilder.create();
		this.builder.setMaxConnTotal(getMaxActive());
		this.builder.setSSLSocketFactory(sslsf);
	}
	@Override
	protected HttpClient create() {
		return builder.build();
	}

	@Override
	protected void clear(HttpClient httpClient) {
		// close 后使用可能会有问题
	}
	
	@Override
	protected void close(HttpClient httpClient) {
		if (httpClient instanceof CloseableHttpClient){
			try {
				((CloseableHttpClient) httpClient).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
