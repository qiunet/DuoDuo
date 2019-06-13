package org.qiunet.logger.sender;

import io.netty.buffer.ByteBuf;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.encryptAndDecrypt.MD5Util;

import java.nio.ByteBuffer;

/**
 * udp 的消息头
 * Created by qiunet.
 * 17/9/14
 */
public class MsgHeader {
	// 头的长度
	public static final int MESSAGE_HEADER_LENGTH = 12;

	private short gameId;     // 游戏id
	private int dt;         // 精确到秒
	private int validKey;  // 鉴权使用. 根据平台分配的secret 加密使用
	private short length;	// 长度
	private MsgHeader(){}
	/***
	 * 校验这个头是否有效
	 * @return
	 */
	public boolean isValidHeader(String secret){
		return getValidKey(dt, secret, length) == validKey;
	}
	public short getGameId() {
		return gameId;
	}
	public short getLength() {
		return length;
	}
	public int getDt() {
		return dt;
	}

	/***
	 * 写一个头到byteBuffer中
	 * @param buffer
	 */
	public static void completeMessageHeader(ByteBuffer buffer, short gameId, String secret, short length) {
		int dt = (int) (DateUtil.currentTimeMillis()/1000);
		buffer.putShort(gameId);
		buffer.putInt(dt);
		buffer.putShort(length);
		buffer.putInt(getValidKey(dt, secret, length));
	}
	/***
	 * 得到一个msgHeader
	 * @param byteBuf
	 * @return
	 */
	public static MsgHeader parseFrom(ByteBuf byteBuf) {
		MsgHeader header = new MsgHeader();

		header.gameId = byteBuf.readShort();
		header.dt = byteBuf.readInt();
		header.length = byteBuf.readShort();
		header.validKey = byteBuf.readInt();

		return header;
	}
	private static final int [] indexs = {2, 16, 7, 15};
	/***
	 * 得到validKey
	 * @return
	 */
	private static int getValidKey(int dt, String secret, int length) {
		String dtString = String.valueOf(dt);
		String key = dtString.substring(6) + length + secret;
		String md5 = MD5Util.MD5(key);
		int ret = 0;
		for (int i = 0; i < indexs.length; i++) {
			ret += (md5.charAt(indexs[i]) << Integer.SIZE - (i+1)*8);
		}
		return ret;
	}

	@Override
	public String toString() {
		return "MsgHeader{" +
				"gameId=" + gameId +
				"length=" + length +
				", dt=" + dt +
				", validKey=" + validKey +
				'}';
	}
}
