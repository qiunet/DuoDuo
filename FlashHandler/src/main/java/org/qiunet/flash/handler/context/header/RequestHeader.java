package org.qiunet.flash.handler.context.header;

/**
 * 请求的固定头
 * Created by qiunet.
 * 17/7/19
 */
public class RequestHeader {
	/**请求头固定长度*/
	public static final int REQUEST_HEADER_LENGTH = 20;
	// 长度
	private int length;
	// 请求序列
	private int sequece;
	// 请求的 命令id
	private int requestId;

	private long crc;

	public RequestHeader(int length, int sequece, int requestId, long crc) {
		this.crc = crc;
		this.length = length;
		this.sequece = sequece;
		this.requestId = requestId;
	}

	public long getCrc() {
		return crc;
	}

	public int getLength() {
		return length;
	}

	public int getSequece() {
		return sequece;
	}

	public int getRequestId() {
		return requestId;
	}
}
