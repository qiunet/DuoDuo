package org.qiunet.cross.transaction;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 * 传输过程的信息.
 *
 * @author qiunet
 * 2020-09-24 09:34
 */
@ProtobufClass
public class RouteTransactionInfo {
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
	@Protobuf(description = "请求数据")
	private BaseTransactionRequest reqData;
	/**
	 * 请求的ServerId
	 */
	@Protobuf
	private int reqServerId;

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

	public BaseTransactionRequest getReqData() {
		return reqData;
	}

	public void setReqData(BaseTransactionRequest reqData) {
		this.reqData = reqData;
	}

	public int getReqServerId() {
		return reqServerId;
	}

	public void setReqServerId(int reqServerId) {
		this.reqServerId = reqServerId;
	}
}
