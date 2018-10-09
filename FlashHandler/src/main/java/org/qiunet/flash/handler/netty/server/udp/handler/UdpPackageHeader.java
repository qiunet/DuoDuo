package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/9/17 11:55
 **/
class UdpPackageHeader {
	// udpheader 占用长度
	static final int UDPHEADER_LENGTH = 10;

	// 类型 0 普通消息. 就是udpHeader的消息. 1 超时消息索要消息  2. 确认消息
	private byte type;

	// 包id 发的包自增的id 对应UdpPackages 一个数据类 id 不能为负数. 保证自增
	private int id;

	// 包的分包id 最大不超过  subCount
	private short subId;

	// 包的分包数  对应构造 UdpPackages(int packageCount) 的构造参数 只有type 0(普通消息)有值 其它类型为0.
	private short subCount;

	// 是否需要确认 需要确认得告诉客户端 或者服务器 避免重传 只有type 0(普通消息)有值  其它类型为0.
	private byte needAck;

	private UdpPackageHeader() {}

	public UdpPackageHeader(byte type, int id, short subId, short subCount, byte needAck) {
		this.type = type;
		this.id = id;
		this.subId = subId;
		this.subCount = subCount;
		this.needAck = needAck;
	}

	/**
	 * 从一个bytebuf读取一个udpheader
	 * @param bytebuf
	 * @return
	 */
	static UdpPackageHeader readUdpHeader(ByteBuf bytebuf) {
		UdpPackageHeader header = new UdpPackageHeader();
		header.type = bytebuf.readByte();
		header.id = bytebuf.readInt();
		header.subId = bytebuf.readShort();
		header.subCount = bytebuf.readShort();
		header.needAck = bytebuf.readByte();
		return header;
	}

	/**
	 * 写入bytebuf
	 * @param byteBuf
	 */
	void writeToByteBuf(ByteBuf byteBuf) {
		byteBuf.writeByte(this.type);
		byteBuf.writeInt(this.id);
		byteBuf.writeShort(this.subId);
		byteBuf.writeShort(this.subCount);
		byteBuf.writeByte(this.needAck);
	}

	public UdpMessageType getType() {
		return UdpMessageType.parse(type);
	}

	public int getId() {
		return id;
	}

	public short getSubId() {
		return subId;
	}

	public short getSubCount() {
		return subCount;
	}

	public byte getNeedAck() {
		return needAck;
	}
}
