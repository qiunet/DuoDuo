package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.utils.exceptions.EnumParseException;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/10/8 14:47
 **/
public enum UdpMessageType {
	/**
	 * 正常的消息
	 */
	NORMAL(0),
	/***
	 * 回应消息
	 */
	ACK(1);
	private byte type;

	UdpMessageType(int type) {
		this.type = (byte)type;
	}

	public byte getType() {
		return type;
	}

	public ByteBuf getMessage(int id, int subId) {
		UdpPackageHeader header = new UdpPackageHeader(this.type, id, (short)subId, (short)0, (byte)0);
		ByteBuf bytebuf = PooledBytebufFactory.getInstance().alloc(UdpPackageHeader.UDPHEADER_LENGTH);
		header.writeToByteBuf(bytebuf);
		return bytebuf;
	}

	public static UdpMessageType parse(int val){
		if (val >= 0 && val < values().length) {
			return values()[val];
		}

		throw new EnumParseException(val);
	}
}
