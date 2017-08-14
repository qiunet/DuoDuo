package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.iodata.base.InputByteDataStream;
import org.qiunet.flash.handler.iodata.base.InputByteStream;
import org.qiunet.flash.handler.iodata.base.OutputByteStream;
import org.qiunet.flash.handler.iodata.constants.IoDataConstants;
import org.qiunet.flash.handler.iodata.log.ILogSwitch;
import org.qiunet.flash.handler.iodata.net.IoData;

/**
 * 请求的固定头
 * Created by qiunet.
 * 17/7/19
 */
public class ProtocolHeader implements IoData {
	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 28;
	/**辨别 请求使用*/
	private byte [] magic;
	// 长度
	private int length;
	// 请求序列
	private int sequence;
	// 请求的 响应的协议 id
	private int protocolId;
	// byte 的加密块大小 (多个块互换)
	private int chunkSize;
	// crc code
	private long crc;

	/***
	 * 构造函数
	 * 不使用datainputstream了.  不确定外面使用的是什么.
	 * 由外面读取后 调构造函数传入
	 * @param length 后面byte数组 长度
	 * @param sequence 请求的序列码
	 * @param protocolId 请求的id
	 * @param chunkSize 加密块的大小
	 * @param crc crc 完整校验
	 */
	public ProtocolHeader(int length, int sequence, int protocolId, int chunkSize, long crc) {
		this.magic = IoDataConstants.magic;
		this.crc = crc;
		this.length = length;
		this.sequence = sequence;
		this.chunkSize = chunkSize;
		this.protocolId = protocolId;
	}

	public ProtocolHeader(){}


	public ProtocolHeader(ByteBuf in) {
		this.magic = new byte[IoDataConstants.magic.length];
		in.readBytes(magic);
		this.length = in.readInt();
		this.sequence = in. readInt();
		this.protocolId = in.readInt();
		this.chunkSize = in.readInt();
		this.crc = in.readLong();
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public long getCrc() {
		return crc;
	}

	public int getLength() {
		return length;
	}

	public int getSequence() {
		return sequence;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public  void writeToByteBuf(ByteBuf out) {
		out.writeBytes(magic);
		out.writeInt(length);
		out.writeInt(sequence);
		out.writeInt(protocolId);
		out.writeInt(chunkSize);
		out.writeLong(crc);
	}

	@Override
	public void dataReader(InputByteStream in) throws Exception {
		((ILogSwitch)in).setPrintLog(false);
		this.magic = in.readBytes(IoDataConstants.magic.length);
		this.length = in.readInt("length");
		this.sequence = in.readInt("sequence");
		this.protocolId = in.readInt("protocolId");
		this.chunkSize = in.readInt("chunkSize");
		this.crc = in.readLong("crc");
		((InputByteDataStream)in).setPrintLog(true);
	}

	@Override
	public void dataWriter(OutputByteStream out) throws Exception {
		((ILogSwitch)out).setPrintLog(false);
		out.writeBytes(IoDataConstants.magic);
		out.writeInt("length", length);
		out.writeInt("sequence", sequence);
		out.writeInt("protocolId", protocolId);
		out.writeInt("chunkSize", chunkSize);
		out.writeLong("crc", crc);
		((ILogSwitch)out).setPrintLog(true);
	}
}
