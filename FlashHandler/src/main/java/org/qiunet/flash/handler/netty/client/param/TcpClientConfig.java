package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.ServerConnType;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpClientConfig extends AbstractClientConfig {
	// 使用默认的连接参数， 端口和地址后面connect时候给出。
	public static final TcpClientConfig DEFAULT_PARAMS = TcpClientConfig.custom().build();
	private TcpClientConfig(){}

	@Override
	public ServerConnType getConnType() {
		return ServerConnType.TCP;
	}

	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		TcpClientConfig params = new TcpClientConfig();
		return params.new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<TcpClientConfig, Builder> {
		@Override
		protected TcpClientConfig newConfig() {
			return TcpClientConfig.this;
		}
	}
}
