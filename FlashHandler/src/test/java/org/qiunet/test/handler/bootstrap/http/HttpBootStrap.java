package org.qiunet.test.handler.bootstrap.http;

import io.netty.util.ResourceLeakDetector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.test.handler.bootstrap.hook.MyHook;
import org.qiunet.test.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class HttpBootStrap {
	protected static final IProtocolHeaderType ADAPTER = ProtocolHeaderType.client;
	protected static final int port = 8090;
	private static final Hook hook = new MyHook();
	private static Thread currThread;
	@BeforeAll
	public static void init() throws Exception {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

		ClassScanner.getInstance(ScannerType.SERVER).scanner();

		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
					.setProtocolHeaderType(ProtocolHeaderType.server)
					.setStartupContext(new StartupContext())
					.setWebsocketPath("/ws")
					.setPort(port)
					.build();
			BootstrapServer server = BootstrapServer.createBootstrap(hook).httpListener(httpParams);
			server.await( () -> {
				LockSupport.unpark(currThread);
			});
		});
		thread.start();
		LockSupport.park();
	}

	/**
	 * 获得所有bytes数据
	 * @return
	 */
	public byte[] getAllBytes(DefaultProtobufMessage message){
		IProtocolHeader header = ADAPTER.outHeader(message.getProtocolID(), message);
		ByteBuffer allocate = ByteBuffer.allocate(ADAPTER.getRspHeaderLength() + message.byteBuffer().limit());
		allocate.put((ByteBuffer) header.dataBytes().rewind());
		allocate.put((ByteBuffer) message.byteBuffer().rewind());
		message.getByteBuf().release();
		return allocate.array();
	}

	@AfterAll
	public static void shutdown(){
		System.gc();
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
