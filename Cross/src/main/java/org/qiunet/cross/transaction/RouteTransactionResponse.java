package org.qiunet.cross.transaction;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;

/***
 * 传输过程的信息.
 *
 * @author qiunet
 * 2020-09-24 09:34
 */
@SkipProtoGenerator
@ProtobufClass(description = "事务响应数据")
public class RouteTransactionResponse implements IpbRequestData {
	/**
	 * 响应端维护自增的id, 需要带着返回给请求服务器.
	 */
	@Protobuf
	private long id;
	/**
	 * 响应数据className
	 * 使用顺序索引无法绝对保证相等.
	 */
	@Protobuf
	private String respClassName;
	/**
	 * 请求数据
	 */
	@Protobuf(description = "响应数据", fieldType = FieldType.BYTES)
	private byte[] respData;

	public static RouteTransactionResponse valueOf(long id, String respClassName, byte[] respData) {
		RouteTransactionResponse response = new RouteTransactionResponse();
		response.id = id;
		response.respClassName = respClassName;
		response.respData = respData;
		return response;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRespClassName() {
		return respClassName;
	}

	public void setRespClassName(String respClassName) {
		this.respClassName = respClassName;
	}

	public byte[] getRespData() {
		return respData;
	}

	public void setRespData(byte[] respData) {
		this.respData = respData;
	}
}