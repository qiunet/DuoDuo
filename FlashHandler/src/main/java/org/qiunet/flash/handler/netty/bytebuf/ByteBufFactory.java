package org.qiunet.flash.handler.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * Bytebuf 使用堆内内存
 * Created by qiunet.
 * 17/11/21
 */
public enum ByteBufFactory {
	instance;
	private PooledByteBufAllocator allocor = PooledByteBufAllocator.DEFAULT;
	public static ByteBufFactory getInstance() {
		return instance;
	}

	/**
	 * 分配一个bytebuf
	 * @return
	 */
	public ByteBuf alloc(){
		return alloc(256);
	}

	/**
	 * 分配一个bytebuf
	 * @param initialCapacity 初始容量
	 * @return
	 */
	public ByteBuf alloc(int initialCapacity){
		return allocor.directBuffer(initialCapacity);
	}
	/***
	 * 使用指定的bytes 分配一个bytebuf
	 * @param bytes
	 * @return
	 */
	public ByteBuf alloc(byte [] bytes){
		return Unpooled.wrappedBuffer(bytes);
	}
}
