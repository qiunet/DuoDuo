package org.qiunet.cross.transaction;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

/***
 * 传输过程的信息.
 *
 * @author qiunet
 * 2020-09-24 09:34
 */
@SkipProtoGenerator
@ServerCommunicationData
@ChannelData(ID = IProtocolId.System.TRANSACTION_REQ, desc = "处理事务请求")
public class RouteTransactionReq implements IChannelData, IDataToString {
	/**
	 * 请求端维护自增的id, 需要带着返回给请求服务器.
	 */
	@Protobuf
	private long id;
	@Protobuf
	private TransferJsonData jsonData;


	static RouteTransactionReq valueOf(long id, ITransactionReq data) {
		RouteTransactionReq request = new RouteTransactionReq();
		request.id = id;
		request.jsonData = new TransferJsonData(data);
		return request;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TransferJsonData getJsonData() {
		return jsonData;
	}

	public void setJsonData(TransferJsonData jsonData) {
		this.jsonData = jsonData;
	}

	public ITransactionReq getData() {
		return (ITransactionReq) jsonData.getData();
	}

	@Override
	public String _toString() {
		return "=TransactionReq= " + ToString.toString(getData())+"]";
	}
}
