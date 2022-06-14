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
@ChannelData(ID = IProtocolId.System.TRANSACTION_RSP, desc = "处理事务请求")
public class RouteTransactionRsp implements IChannelData, IDataToString {
	/**
	 * 响应端维护自增的id, 需要带着返回给请求服务器.
	 */
	@Protobuf
	private long id;
	@Protobuf
	private TransferJsonData jsonData;

	public static RouteTransactionRsp valueOf(long id, ITransactionRsp data) {
		RouteTransactionRsp response = new RouteTransactionRsp();
		response.jsonData = new TransferJsonData(data);
		response.id = id;
		return response;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClassName(){
		return jsonData.getClazz();
	}

	public ITransactionRsp getData() {
		return (ITransactionRsp) jsonData.getData();
	}

	@Override
	public String _toString() {
		return "=TransactionRsp= " + ToString.toString(getData())+"]";
	}
}
