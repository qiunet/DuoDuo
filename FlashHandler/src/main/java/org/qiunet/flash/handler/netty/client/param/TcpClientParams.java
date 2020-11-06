package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.HandlerType;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpClientParams extends AbstractClientParam {
	public static final TcpClientParams DEFAULT_PARAMS = TcpClientParams.custom().build();
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
		TcpClientParams params = new TcpClientParams();
		return params.new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<TcpClientParams, Builder> {

		@Override
		protected TcpClientParams newParams() {
			return TcpClientParams.this;
		}
	}
}
