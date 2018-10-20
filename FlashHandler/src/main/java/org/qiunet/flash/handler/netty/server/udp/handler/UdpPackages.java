package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;

import java.util.ArrayList;

/***
 * 发送一个完整包. 分割成N个小包
 * 并且记录时间. 使用定时器 .超时重传.
 * @Author qiunet
 * @Date Create in 2018/7/20 15:18
 **/
public class UdpPackages {
	// 最大的udp包单包byte数 国际标准 - ip包 - udp头 - 自定义header头
	private static final int MAX_UDP_PACKAGE_BYTE_COUNTS = (576 - 20 - 8 - UdpPackageHeader.UDPHEADER_LENGTH);

	private int id;
	// 包生成时间 用来计算重传. 2秒后. 会要求再次重传某个包
	private long dt;

	private boolean importantMsg;
	// 重传的次数.
	private int resendCount;
	// 当前包内有多少数据包.
	private int totalPackageCount;
	// 一个bytebuf数组.
	ArrayList<byte []> byteArrs;
	/***
	 * 使用包数. 构造一个udpPackages
	 * @param packageCount
	 */
	public UdpPackages(int id, int packageCount) {
		if (packageCount < 1) throw new IllegalArgumentException("packageCount can not less than 1");
		this.dt = System.currentTimeMillis();
		this.totalPackageCount = packageCount;
		this.byteArrs = new ArrayList<>(this.totalPackageCount);
		for (int i = 0; i < this.totalPackageCount; i++) {
			this.byteArrs.add(null);
		}
	}

	/**
	 * 使用bytebuf 分包.
	 * @param bytebuf
	 */
	public UdpPackages(int id, boolean importantMsg, ByteBuf bytebuf) {
		this.dt = System.currentTimeMillis();
		this.byteArrs = new ArrayList<>();
		this.importantMsg = importantMsg;
		this.id = id;
		do {
			int readCount = Math.min(MAX_UDP_PACKAGE_BYTE_COUNTS, bytebuf.readableBytes());
			byte [] bytes = new byte[readCount];
			bytebuf.readBytes(bytes);
			this.byteArrs.add(bytes);

			this.totalPackageCount++;
		}while (bytebuf.readableBytes() > 0);
		ReferenceCountUtil.release(bytebuf);
	}
	/**
	 * add 一个新的udp包.
	 * 如果已经是一个完整的协议包. 则返回协议包的bytebuf
	 */
	public ByteBuf addNewPartMessage(UdpPackageHeader header, byte[] content) {
		this.byteArrs.set(header.getSubId(), content);
		int byteLength = 0;
		for (int index = this.totalPackageCount - 1; index >= 0; index--) {
			if (this.byteArrs.get(index) == null) return null;

			byteLength += this.byteArrs.get(index).length;
		}
		ByteBuf byteBuf = PooledBytebufFactory.getInstance().alloc(byteLength);
		for (byte[] bytes : this.byteArrs) {
			byteBuf.writeBytes(bytes);
		}
		return byteBuf;
	}

	/***
	 * 组装真的消息.
	 * @param index
	 * @return
	 */
	public ByteBuf composeRealMessage(int index) {
		byte[] bytes = this.byteArrs.get(index);
		ByteBuf byteBuf = PooledBytebufFactory.getInstance().alloc(UdpPackageHeader.UDPHEADER_LENGTH + bytes.length);
		UdpPackageHeader header = new UdpPackageHeader(UdpMessageType.NORMAL.getType(), this.id, (short)index, (short)this.totalPackageCount, (byte)(this.importantMsg ? 1 : 0));
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(this.byteArrs.get(index));
		return byteBuf;
	}

	public int getId() {
		return id;
	}

	public long getDt() {
		return dt;
	}

	public int getResendCount() {
		return resendCount;
	}

	public void retainSendCount(){
		this.resendCount++;
	}

	public int getTotalPackageCount() {
		return totalPackageCount;
	}
}
