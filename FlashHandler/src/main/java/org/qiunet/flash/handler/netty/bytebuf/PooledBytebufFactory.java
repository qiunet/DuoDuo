package org.qiunet.flash.handler.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Bytebuf 使用堆内内存
 * Created by qiunet.
 * 17/11/21
 */
public class PooledBytebufFactory {

	private PooledByteBufAllocator allocor;

	private volatile static PooledBytebufFactory instance;

	private PooledBytebufFactory() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		allocor = PooledByteBufAllocator.DEFAULT;
		instance = this;
	}

	public static PooledBytebufFactory getInstance() {
		if (instance == null) {
			synchronized (PooledBytebufFactory.class) {
				if (instance == null)
				{
					new PooledBytebufFactory();
				}
			}
		}
		return instance;
	}

	/**
	 * 分配一个bytebuf
	 * @return
	 */
	public ByteBuf alloc(){
		return allocor.buffer();
	}

	/***
	 * 使用指定的bytes 分配一个bytebuf
	 * @param bytes
	 * @return
	 */
	public ByteBuf alloc(byte [] bytes){
		ByteBuf bytebuf =  allocor.buffer(bytes.length);
		bytebuf.writeBytes(bytes);
		return bytebuf;
	}
}
