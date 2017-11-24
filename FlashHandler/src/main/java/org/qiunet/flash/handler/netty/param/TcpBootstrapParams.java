package org.qiunet.flash.handler.netty.param;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {
	private int maxReceivedLength;
	private TcpBootstrapParams(){}

	public int getMaxReceivedLength() {
		return maxReceivedLength;
	}

	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		return new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public static class Builder extends SuperBuilder<TcpBootstrapParams, Builder> {
		// 最大上行1M的长度
		private int maxReceivedLength = 1024 * 1024;

		 private Builder(){}

		public Builder setMaxReceivedLength(int maxReceivedLength) {
			this.maxReceivedLength = maxReceivedLength;
			return this;
		}

		@Override
		protected TcpBootstrapParams newParams() {
			return new TcpBootstrapParams();
		}

		@Override
		protected void buildInner(TcpBootstrapParams tcpBootstrapParams) {
			tcpBootstrapParams.maxReceivedLength = this.maxReceivedLength;
		}
	}
}
