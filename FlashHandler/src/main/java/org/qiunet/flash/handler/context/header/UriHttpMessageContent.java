package org.qiunet.flash.handler.context.header;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class UriHttpMessageContent extends MessageContent {
	private String uriPath;

	public UriHttpMessageContent(String uriPath, byte [] bytes) {
		super(0 , bytes);
		this.uriPath = uriPath;
	}

	public String getUriPath() {
		return uriPath;
	}
}
