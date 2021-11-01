package org.qiunet.cross.transaction;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.IDataToString;
import org.qiunet.flash.handler.util.SkipProtoGenerator;
import org.qiunet.utils.json.JsonUtil;

/***
 * 传输过程的信息.
 *
 * @author qiunet
 * 2020-09-24 09:34
 */
@SkipProtoGenerator
@ChannelData(ID = IProtocolId.System.TRANSACTION_RESP, desc = "处理事务请求")
public class RouteTransactionResponse implements IChannelData, IDataToString {
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
	/**
	 * 供打印使用的data
	 */
	@Ignore
	private BaseTransactionResponse data;

	public static RouteTransactionResponse valueOf(long id, BaseTransactionResponse data) {
		RouteTransactionResponse response = new RouteTransactionResponse();
		response.respData = ProtobufDataManager.encode(data);
		response.respClassName = data.getClass().getName();
		response.data = data;
		response.id = id;
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

	public BaseTransactionResponse getData() {
		if (data == null) {
			try {
				Class<? extends BaseTransactionResponse>aClass = (Class<? extends BaseTransactionResponse>) Class.forName(respClassName);
				data = ProtobufDataManager.decode(aClass, respData);
			} catch (Exception e) {
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
