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
@ProtobufClass(description = "事务请求类")
public class RouteTransactionRequest implements IpbRequestData {
	/**
	 * 请求端维护自增的id, 需要带着返回给请求服务器.
	 */
	@Protobuf
	private long id;
	/**
	 * 请求数据className
	 * 使用顺序索引无法绝对保证相等.
	 */
	@Protobuf
	private String reqClassName;
	/**
	 * 请求数据
	 */
	@Protobuf(description = "请求数据", fieldType = FieldType.BYTES)
	private byte [] reqData;


	static RouteTransactionRequest valueOf(long id, String reqClassName, byte[] reqData) {
		RouteTransactionRequest request = new RouteTransactionRequest();
		request.id = id;
		request.reqData = reqData;
		request.reqClassName = reqClassName;
		return request;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReqClassName() {
		return reqClassName;
	}

	public void setReqClassName(String reqClassName) {
		this.reqClassName = reqClassName;
	}

	public byte [] getReqData() {
		return reqData;
	}

	public void setReqData(byte [] reqData) {
		this.reqData = reqData;
	}
}
