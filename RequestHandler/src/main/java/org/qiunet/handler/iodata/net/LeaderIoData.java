package org.qiunet.handler.iodata.net;

import org.qiunet.handler.iodata.base.InputByteDataStream;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.constants.IoDataConstants;
import org.qiunet.handler.log.ILogSwitch;

/**
 * @author qiunet
 *         Created on 17/3/10 15:24.
 */
public final class LeaderIoData implements IoData {
	/**头的长度*/
	public static final int LEADER_DATA_LENGTH = IoDataConstants.magic.length + 2 + 2 + 8 + 4;
	
	/**辨别 请求使用*/
	private byte [] magic;
	/**请求是requestId , 响应时候是responseId*/
	private short cmdId;
	/**乱序防作弊使用的*/
	private short split;
	/**crc 校验*/
	private long crc;
	/**长度*/
	private int length;
	@Override
	public void dataReader(InputByteStream in) throws Exception {
		((ILogSwitch)in).setPrintLog(false);
		this.magic = in.readBytes(IoDataConstants.magic.length);
		this.cmdId = in.readShort("cmdId");
		this.split = in.readShort("split");
		this.crc = in.readLong("crc");
		this.length = in.readInt("length");
		((InputByteDataStream)in).setPrintLog(true);
	}

	@Override
	public void dataWriter(OutputByteStream out) throws Exception {
		((ILogSwitch)out).setPrintLog(false);
		out.writeBytes(IoDataConstants.magic);
		out.writeShort("cmdId", cmdId);
		out.writeShort("split", split);
		out.writeLong("crc", crc);
		out.writeInt("length", length);
		((ILogSwitch)out).setPrintLog(true);
	}
	
	
	public short getCmdId() {
		return cmdId;
	}

	public void setCmdId(short cmdId) {
		this.cmdId = cmdId;
	}

	public short getSplit() {
		return split;
	}

	public void setSplit(short split) {
		this.split = split;
	}

	public long getCrc() {
		return crc;
	}

	public void setCrc(long crc) {
		this.crc = crc;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
