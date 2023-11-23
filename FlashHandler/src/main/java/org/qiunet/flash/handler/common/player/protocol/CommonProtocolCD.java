package org.qiunet.flash.handler.common.player.protocol;

/**
 * 处理协议请求的CD
 * @author qiunet
 * 2023/11/21 21:00
 */
public class CommonProtocolCD {
	/**
	 * 最后一个请求协议ID.
	 */
	private int protocolId;
	/**
	 * 请求处理时间 毫秒
	 */
	private long lastDt;

	/**
	 * 是否触发cd限制
	 * @param protocolId 协议id
	 * @param now 当前时间
	 * @return true 触发. false 没问题.
	 */
	public boolean isInvalidRequest(int protocolId, long now) {
		try {
			return this.protocolId == protocolId && now - lastDt <= 10;
		}finally {
			this.protocolId = protocolId;
			this.lastDt = now;
		}
	}
}
