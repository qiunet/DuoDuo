package org.qiunet.test.handler.bootstrap.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.ResourceLeakDetector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.test.handler.bootstrap.hook.MyHook;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class HttpBootStrap {
	protected static final IProtocolHeader PROTOCOL_HEADER = DefaultProtocolHeader.instance;
	protected static final int port = 8090;
	private static final Hook hook = new MyHook();
	private static Thread currThread;
	@BeforeAll
	public static void init() throws Exception {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			BootstrapServer server = BootstrapServer.createBootstrap(hook, RedisDataUtil::getInstance)
				.listener(ServerBootStrapConfig.newBuild("Http测试", port).build());
			server.await( () -> LockSupport.unpark(currThread));
		});
		thread.start();
		LockSupport.park();
	}

	/**
	 * 获得所有bytes数据
	 * @return
	 */
	public byte[] getAllBytes(DefaultProtobufMessage message){
		IProtocolHeader.IClientOutHeader header = PROTOCOL_HEADER.clientNormalOut(message, null);
		ByteBuffer allocate = ByteBuffer.allocate(PROTOCOL_HEADER.getClientOutHeadLength() + message.byteBuffer().limit());
		ByteBuf headerByteBuf = header.headerByteBuf();
		allocate.put(headerByteBuf.nioBuffer().rewind());
		allocate.put(message.byteBuffer().rewind());
		headerByteBuf.release();
		message.getByteBuf().release();
		return allocate.array();
	}

	@AfterAll
	public static void shutdown(){
		System.gc();
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
