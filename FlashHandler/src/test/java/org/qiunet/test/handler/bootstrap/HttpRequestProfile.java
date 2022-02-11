package org.qiunet.test.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.test.handler.bootstrap.hook.MyHook;
import org.qiunet.test.handler.proto.ProtocolId;
import org.qiunet.test.handler.startup.context.StartupContext;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.nio.ByteBuffer;

/**
 * 大量请求性能
 * Created by qiunet.
 * 17/11/27
 */
@Fork(1)
@State(Scope.Thread)
@Measurement(iterations = 5)
@Warmup(iterations = 1, time = 5)
public class HttpRequestProfile {
	private static final IProtocolHeaderType ADAPTER = ProtocolHeaderType.client;
	public static final Hook hook = new MyHook();
	private static final int threadCount = 16;
	public static final int port = 8080;
	private static final HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", port)
		.setProtocolHeaderType(ADAPTER)
		.build();


	@Benchmark
	@Threads(threadCount)
	public String httpRequest() {
		final String test = "[测试testHttpProtobuf]";
		MessageContent content = new MessageContent(ProtocolId.Test.HTTP_PB_LOGIN_REQ, test.getBytes(CharsetUtil.UTF_8));
		return HttpRequest.post(params.getURI())
			.withBytes(ADAPTER.getAllBytes(content.getProtocolId(), content.bytes()))
			.executor(resp -> {
				ByteBuffer buffer = ByteBuffer.wrap(resp.body().bytes());
				buffer.position(ADAPTER.getReqHeaderLength());
				return CharsetUtil.UTF_8.decode(buffer).toString();
			});
	}

	public static void serverStartup(){
		ClassScanner.getInstance(ScannerType.SERVER).scanner();

		HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
			.setProtocolHeaderType(ProtocolHeaderType.server)
			.setStartupContext(new StartupContext())
			.setWebsocketPath("/ws")
			.setPort(port)
			.build();

		new Thread(() -> {
			BootstrapServer server = BootstrapServer.createBootstrap(hook).httpListener(httpParams);
			server.await();
		}).start();
	}

	public static void main(String[] args) throws RunnerException, InterruptedException {
		serverStartup();
		new Runner(
			new OptionsBuilder()
				.include(HttpRequestProfile.class.getSimpleName())
				.jvmArgsAppend("-Dfile.encoding=UTF-8")
				.addProfiler(HttpJmhProfile.class)
				.jvmArgs("-server")
				.syncIterations(false)
				.build()
		).run();

	}
}
