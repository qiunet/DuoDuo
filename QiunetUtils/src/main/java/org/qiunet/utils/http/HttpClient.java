package org.qiunet.utils.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

import static org.qiunet.utils.http.HttpChannelManager.REQ_DATA_KEY;

/***
 * 自己实现的http client
 *
 * @author qiunet
 * 2020-04-20 17:39
 ***/
enum HttpClient {
	instance;
	/**
	 * 执行http 请求
	 * @param requestData 请求数据
	 */
	Promise<HttpResponse> request(HttpRequestData requestData){
		String protocol = requestData.getUrl().getProtocol();
		if (! "http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
			throw new RuntimeException("Url ["+ requestData.getUrl()+"] not a valid http request!");
		}

		Future<Channel> future = requestData.getPool().acquire();
		future.addListener(f -> {
			if (! f.isSuccess()) {
				requestData.fail(f.cause());
				return;
			}
			Channel channel = (Channel) f.get();
			channel.attr(REQ_DATA_KEY).set(requestData);
			ChannelFuture channelFuture = channel.writeAndFlush(requestData.getRequest());
			channelFuture.addListener(f0 -> {
				if (! f0.isSuccess()) {
					requestData.fail(f0.cause());
				}
			});
		});
		return requestData.getRspPromise();
	}
}
