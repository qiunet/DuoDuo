package org.qiunet.flash.handler.bootstrap;

import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultHttpStringInterceptor;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.BootstrapServer;

/**
 * 测试接收数据
 * Created by qiunet.
 * 17/11/22
 */
public class TestBootStrap extends RequestHandlerScanner {
	public static void main(String[] args) {

		HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
				.setInterceptor(new DefaultHttpStringInterceptor())
				.setPort(8080)
				.build();
		BootstrapServer.createBootstrap(new MyHook()).httpListener(httpParams).await();
	}

}
