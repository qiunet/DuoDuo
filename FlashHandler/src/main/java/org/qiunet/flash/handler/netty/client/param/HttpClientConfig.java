package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.ServerConnType;

/**
 * 使用引导类 参数.
 * 该模式通用 websocket
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public class HttpClientConfig extends AbstractClientConfig {
	/**
	 * 是否使用ssl
	 */
	private boolean ssl;
	/***
	 * 如果uri 匹配, 则会在字节数据前加入 IProtocolHeader 信息
	 */
	private String uriPath = "/f";

	private HttpClientConfig(){}

	public boolean isSsl() {
		return ssl;
	}

	public String getUriPath() {
		return uriPath;
	}

	public String getURI(){
		return getURI(this.uriPath);
	}

	public String getURI(String pathAndParam){
		StringBuilder sb = new StringBuilder("http");
		if (ssl) sb.append("s");
		sb.append("://").append(address.getHostString());
		if ((ssl && address.getPort() != 443)
			|| (!ssl && address.getPort() != 80)) {
			sb.append(":").append(address.getPort());
		}
		if (! pathAndParam.startsWith("/")) pathAndParam = "/" + pathAndParam;
		sb.append(pathAndParam);
		return sb.toString();
	}

	@Override
	public ServerConnType getConnType() {
		return ServerConnType.HTTP;
	}
	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		HttpClientConfig params = new HttpClientConfig();
		return params.new Builder();
	}
	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public class Builder extends SuperBuilder<HttpClientConfig, Builder> {
		private Builder(){}

		public Builder setUriPath(String uriPath) {
			HttpClientConfig.this.uriPath = uriPath;
			return this;
		}

		public Builder setSsl(boolean ssl) {
			HttpClientConfig.this.ssl = ssl;
			return this;
		}

		@Override
		protected HttpClientConfig newConfig() {
			return HttpClientConfig.this;
		}
	}
}
