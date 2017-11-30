package org.qiunet.flash.handler.common.enums;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;

import java.net.URI;

import static io.netty.handler.codec.http.websocketx.WebSocketVersion.*;
import static io.netty.handler.codec.http.websocketx.WebSocketVersion.V00;

/**
 * websocket的版本
 * Created by qiunet.
 * 17/11/30
 */
public enum  WebSocketVersion {

	VERSION_00(io.netty.handler.codec.http.websocketx.WebSocketVersion.V00),

	VERSION_07(io.netty.handler.codec.http.websocketx.WebSocketVersion.V07),

	VERSION_08(io.netty.handler.codec.http.websocketx.WebSocketVersion.V08),

	VERSION_13(io.netty.handler.codec.http.websocketx.WebSocketVersion.V13),
	;

	private io.netty.handler.codec.http.websocketx.WebSocketVersion version;
	private WebSocketVersion(io.netty.handler.codec.http.websocketx.WebSocketVersion version) {
		this.version = version;
	}

	/***
	 * @return Version of web socket
	 */
	public io.netty.handler.codec.http.websocketx.WebSocketVersion getVersion() {
		return version;
	}

	/**
	 * Creates a new handshaker.
	 *
	 * @param webSocketURL
	 *            URL for web socket communications. e.g "ws://myhost.com/mypath".
	 *            Subsequent web socket frames will be sent to this URL.
	 * @param subprotocol
	 *            Sub protocol request sent to the server. Null if no sub-protocol support is required.
	 * @param allowExtensions
	 *            Allow extensions to be used in the reserved bits of the web socket frame
	 * @param customHeaders
	 *            Custom HTTP headers to send during the handshake
	 */
	public WebSocketClientHandshaker newHandshaker(
			URI webSocketURL, String subprotocol,
			boolean allowExtensions, HttpHeaders customHeaders) {
		return newHandshaker(webSocketURL, subprotocol, allowExtensions, customHeaders, 65536);
	}

	/**
	 * Creates a new handshaker.
	 *
	 * @param webSocketURL
	 *            URL for web socket communications. e.g "ws://myhost.com/mypath".
	 *            Subsequent web socket frames will be sent to this URL.
	 * @param subprotocol
	 *            Sub protocol request sent to the server. Null if no sub-protocol support is required.
	 * @param allowExtensions
	 *            Allow extensions to be used in the reserved bits of the web socket frame
	 * @param customHeaders
	 *            Custom HTTP headers to send during the handshake
	 * @param maxFramePayloadLength
	 *            Maximum allowable frame payload length. Setting this value to your application's
	 *            requirement may reduce denial of service attacks using long data frames.
	 */
	public WebSocketClientHandshaker newHandshaker(
			URI webSocketURL, String subprotocol,
			boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength) {
		return newHandshaker(webSocketURL, subprotocol, allowExtensions, customHeaders,
				maxFramePayloadLength, true, false);
	}

	/**
	 * Creates a new handshaker.
	 *
	 * @param webSocketURL
	 *            URL for web socket communications. e.g "ws://myhost.com/mypath".
	 *            Subsequent web socket frames will be sent to this URL.
	 * @param subprotocol
	 *            Sub protocol request sent to the server. Null if no sub-protocol support is required.
	 * @param allowExtensions
	 *            Allow extensions to be used in the reserved bits of the web socket frame
	 * @param customHeaders
	 *            Custom HTTP headers to send during the handshake
	 * @param maxFramePayloadLength
	 *            Maximum allowable frame payload length. Setting this value to your application's
	 *            requirement may reduce denial of service attacks using long data frames.
	 * @param performMasking
	 *            Whether to mask all written websocket frames. This must be set to true in order to be fully compatible
	 *            with the websocket specifications. Client applications that communicate with a non-standard server
	 *            which doesn't require masking might set this to false to achieve a higher performance.
	 * @param allowMaskMismatch
	 *            When set to true, frames which are not masked properly according to the standard will still be
	 *            accepted.
	 */
	public WebSocketClientHandshaker newHandshaker(
			URI webSocketURL, String subprotocol,
			boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength,
			boolean performMasking, boolean allowMaskMismatch) {
		if (version == V13) {
			return new WebSocketClientHandshaker13(
					webSocketURL, V13, subprotocol, allowExtensions, customHeaders,
					maxFramePayloadLength, performMasking, allowMaskMismatch);
		}
		if (version == V08) {
			return new WebSocketClientHandshaker08(
					webSocketURL, V08, subprotocol, allowExtensions, customHeaders,
					maxFramePayloadLength, performMasking, allowMaskMismatch);
		}
		if (version == V07) {
			return new WebSocketClientHandshaker07(
					webSocketURL, V07, subprotocol, allowExtensions, customHeaders,
					maxFramePayloadLength, performMasking, allowMaskMismatch);
		}
		if (version == V00) {
			return new WebSocketClientHandshaker00(
					webSocketURL, V00, subprotocol, customHeaders, maxFramePayloadLength);
		}

		throw new WebSocketHandshakeException("Protocol version " + version + " not supported.");
	}
}
