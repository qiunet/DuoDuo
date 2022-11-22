package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.utils.math.MathUtil;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class KcpClientConfig extends AbstractClientConfig {
	// 使用默认的连接参数， 端口和地址后面connect时候给出。
	public static final KcpClientConfig DEFAULT_CLIENT_CONFIG = KcpClientConfig.custom().build();
	private KcpClientConfig(){}

	/**
	 * 会话ID
	 */
	private int convId;

	@Override
	public ServerConnType getConnType() {
		return ServerConnType.KCP;
	}

	public int getConvId() {
		if (convId == 0) {
			return MathUtil.random(Integer.MAX_VALUE);
		}
		return convId;
	}

	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		KcpClientConfig params = new KcpClientConfig();
		return params.new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<KcpClientConfig, Builder> {

		public Builder setConvId(int convId) {
			KcpClientConfig.this.convId = convId;
			return this;
		}

		@Override
		protected KcpClientConfig newConfig() {
			return KcpClientConfig.this;
		}
	}
}
