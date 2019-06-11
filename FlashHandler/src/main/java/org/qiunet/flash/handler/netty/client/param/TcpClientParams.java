package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.HandlerType;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpClientParams extends AbstractClientParam {

	private TcpClientParams(){}

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.TCP;
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
	public static class Builder extends SuperBuilder<TcpClientParams, Builder> {

		@Override
		protected TcpClientParams newParams() {
			return new TcpClientParams();
		}

		@Override
		protected void buildInner(TcpClientParams tcpClientParams) {
		}
	}
}
