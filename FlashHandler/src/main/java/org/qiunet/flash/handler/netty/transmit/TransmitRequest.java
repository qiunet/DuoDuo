package org.qiunet.flash.handler.netty.transmit;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;

/***
 * 客户端请求的转发
 *
 * @author qiunet
 * 2020-10-26 18:29
 */
@SkipProtoGenerator
@ProtobufClass(description = "客户端请求的转发")
public class TransmitRequest implements IpbRequestData {
	@Protobuf(description = "协议id")
	private int pid;
	@Protobuf(description = "协议内容")
	private byte[] bytes;

	public static TransmitRequest valueOf(int pid, byte[] bytes) {
		TransmitRequest request = new TransmitRequest();
		request.pid = pid;
		request.bytes = bytes;
		return request;

	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
