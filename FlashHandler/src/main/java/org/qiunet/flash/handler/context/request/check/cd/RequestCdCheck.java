package org.qiunet.flash.handler.context.request.check.cd;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.context.request.check.IRequestCheck;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.cd.TouchTimer;

/***
 *
 * @author qiunet
 * 2022/2/8 10:08
 */
public class RequestCdCheck implements IRequestCheck {

	private static final AttributeKey<TouchTimer> timerAttribute = AttributeKey.newInstance("REQUEST_CD_CHECK");
	/**
	 * 需要监视cd的协议ID
	 */
	private final int protocolId;
	/**
	 * cd 配置数据
	 */
	private final RequestCD requestCD;

	public RequestCdCheck(int protocolId, RequestCD requestCD) {
		this.protocolId = protocolId;
		this.requestCD = requestCD;
	}

	@Override
	public void check(Channel channel, IChannelData data) {
		TouchTimer timer;
		if ((timer = channel.attr(timerAttribute).get()) == null) {
			timer = channel.attr(timerAttribute).setIfAbsent(new TouchTimer());
		}
		if (timer.isCding(protocolId, requestCD.value(), requestCD.unit(), requestCD.limitCount())) {
			throw StatusResultException.valueOf(IGameStatus.REQUEST_CD_ING);
		}
	}
}
