package org.qiunet.utils.http;

import io.netty.handler.codec.http.*;
import org.qiunet.utils.data.ByteUtil;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/***
 * http 响应
 * @author qiunet
 * 2024/1/15 19:44
 ***/
public class HttpResponse {

	private final HttpResponseStatus status;

	private final FullHttpRequest request;

	private final HttpHeaders headers;

	private final HttpVersion version;

	private final byte [] bytes;

	private final URL url;
	public HttpResponse(FullHttpRequest request, FullHttpResponse response, URL url) {
		this.bytes = ByteUtil.readBytebuffer(response.content().nioBuffer());
		this.version = response.protocolVersion();
		this.headers = response.headers();
		this.status = response.status();
		this.request = request;
		this.url = url;
	}

	public URL getRequestUrl() {
		return url;
	}


	public FullHttpRequest getRequest() {
		return request;
	}

	public String body() {
		return body(StandardCharsets.UTF_8);
	}

	public String body(Charset charset) {
		return new String(bytes, charset);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public HttpResponseStatus getStatus() {
		return status;
	}

	/**
	 * Returns the protocol version of response
	 */
	public HttpVersion protocolVersion() {
		return this.version;
	}

	/**
	 * Returns the headers of this message.
	 */
	public HttpHeaders headers() {
		return this.headers;
	}
}
