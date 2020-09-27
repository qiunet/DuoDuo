package org.qiunet.flash.handler.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.qiunet.utils.exceptions.CustomException;

/**
 * Bytebuf 使用堆内内存
 * Created by qiunet.
 * 17/11/21
 */
public class PooledBytebufFactory {

	private PooledByteBufAllocator allocor;

	private volatile static PooledBytebufFactory instance;

	private PooledBytebufFactory() {
		if (instance != null) throw new CustomException("Instance Duplication!");
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
		ByteBuf bytebuf =  alloc(bytes.length);
		bytebuf.writeBytes(bytes);
		return bytebuf;
	}
}
