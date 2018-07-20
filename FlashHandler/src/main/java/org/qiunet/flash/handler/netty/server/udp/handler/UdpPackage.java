package org.qiunet.flash.handler.netty.server.udp.handler;

import io.netty.buffer.ByteBuf;

/***
 * 发送一个完整包. 分割成N个小包
 * 并且记录时间. 使用定时器 .超时重传.
 * @Author qiunet
 * @Date Create in 2018/7/20 15:18
 **/
public class UdpPackage implements Runnable{
	// 包生成时间
	private long dt;
	// 重传的次数.
	private int resendCount;

	/****
	 *
	 * @param content
	 */
	public UdpPackage(ByteBuf content) {
		this.dt = System.currentTimeMillis();
	}

	@Override
	public void run() {

	}
}
