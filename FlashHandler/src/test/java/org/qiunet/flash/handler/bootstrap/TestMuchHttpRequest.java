package org.qiunet.flash.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;
import org.qiunet.flash.handler.startup.context.StartupContext;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.scanner.ClassScanner;

import java.nio.ByteBuffer;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
@Fork(1)
@State(Scope.Thread)
@Measurement(iterations = 5)
@Warmup(iterations = 1, time = 5)
public class TestMuchHttpRequest {
	private static final IProtocolHeaderAdapter ADAPTER = new DefaultProtocolHeaderAdapter();
	public static final Hook hook = new MyHook();
	private static final int threadCount = 16;
	public static final int port = 8080;
	private static final HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", port)
		.setProtocolHeaderAdapter(ADAPTER)
		.build();


	@Benchmark
	@Threads(threadCount)
	public String httpRequest() {
		final String test = "[测试testHttpProtobuf]";
		MessageContent content = new MessageContent(5000, test.getBytes(CharsetUtil.UTF_8));
		return HttpRequest.post(params.getURI())
			.withBytes(ADAPTER.getAllBytes(content))
			.executor(resp -> {
				ByteBuffer buffer = ByteBuffer.wrap(resp.body().bytes());
				ADAPTER.newHeader(buffer);
				return CharsetUtil.UTF_8.decode(buffer).toString();
			});
	}

	public static void serverStartup(){
		ClassScanner.getInstance().scanner();

		HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
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
				.include(TestMuchHttpRequest.class.getSimpleName())
				.jvmArgsAppend("-Dfile.encoding=UTF-8")
				.addProfiler(HttpJmhProfile.class)
				.jvmArgs("-server")
				.syncIterations(false)
				.build()
		).run();

	}
}
