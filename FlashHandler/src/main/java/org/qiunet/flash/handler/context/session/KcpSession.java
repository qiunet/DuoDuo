package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.utils.exceptions.CustomException;

/***
 * Kcp 通道的session
 *
 * @author qiunet
 * 2022/4/26 15:46
 */
public class KcpSession extends BaseChannelSession {
	/**
	 * 依赖tcp
	 */
	private boolean dependOnTcp;

	public KcpSession(Channel channel) {
		this.setChannel(channel);
	}

	void setDependOnTcp() {
		this.dependOnTcp = true;
	}

	@Override
	public void close(CloseCause cause) {
		if (dependOnTcp) {
			this.closeChannelOnly(cause);
		}else {
			super.close(cause);
		}
	}

	@Override
	public void addCloseListener(String name, SessionCloseListener listener) {
		if (dependOnTcp) {
			throw new CustomException("Not support!");
		}
		super.addCloseListener(name, listener);
	}

	/**
	 * 仅关闭channel
	 * @param cause 原因
	 */
	private void closeChannelOnly(CloseCause cause) {
		if (! closed.compareAndSet(false, true)) {
			return;
		}
		this.closeChannel(cause);
	}
}
