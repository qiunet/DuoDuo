package org.qiunet.flash.handler.netty.server.param;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {
	private TcpBootstrapParams(){}

	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		TcpBootstrapParams params = new TcpBootstrapParams();
		return params.new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<TcpBootstrapParams, Builder> {

		private Builder(){}

		@Override
		protected TcpBootstrapParams newParams() {
			if (protocolHeaderType == null) {
				throw new NullPointerException("Must set IProtocolHeaderType for Listener!");
			}

			return TcpBootstrapParams.this;
		}
	}
}
