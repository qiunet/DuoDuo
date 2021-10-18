package org.qiunet.cross.transaction;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;
import org.qiunet.utils.json.JsonUtil;

/***
 * 传输过程的信息.
 *
 * @author qiunet
 * 2020-09-24 09:34
 */
@SkipProtoGenerator
@PbChannelData(ID = IProtocolId.System.TRANSACTION_REQ, desc = "处理事务请求")
public class RouteTransactionRequest implements IpbChannelData {
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
	/**
	 * 原始对象
	 */
	@Ignore
	private BaseTransactionRequest data;


	static RouteTransactionRequest valueOf(long id, BaseTransactionRequest data) {
		RouteTransactionRequest request = new RouteTransactionRequest();
		request.id = id;
		request.data = data;
		request.reqClassName = data.getClass().getName();
		request.reqData = ProtobufDataManager.encode(data);
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

	public BaseTransactionRequest getData() {
		if (data == null) {
			try {
				Class<? extends BaseTransactionRequest>aClass = (Class<? extends BaseTransactionRequest>) Class.forName(reqClassName);
				data = ProtobufDataManager.decode(aClass, reqData);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public String _toString() {
		return "Transaction: ["+getData().getClass().getSimpleName()+": " + JsonUtil.toJsonString(getData())+"]";
	}
}
