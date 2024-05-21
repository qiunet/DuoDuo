package org.qiunet.cross.rpc;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.protocol.IgnoreCommonProtocolCDCheck;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.string.ToString;

/***
 *
 * @author qiunet
 * 2023/5/5 14:27
 */
@SkipProtoGenerator
@ServerCommunicationData
@IgnoreCommonProtocolCDCheck
@ChannelData(ID = IProtocolId.System.ROUTE_RPC_RSP, desc = "rpc response")
public class RouteRpcRsp extends IChannelData {
	@Protobuf(description = "请求id")
	private int reqId;
	@Protobuf(description = "json 数据")
	private TransferJsonData jsonData;

	public static RouteRpcRsp valueOf(int reqId, Object rspData){
		RouteRpcRsp data = new RouteRpcRsp();
		data.jsonData = new TransferJsonData(rspData);
		data.reqId = reqId;
		return data;
	}

	public int getReqId() {
		return reqId;
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	public TransferJsonData getJsonData() {
		return jsonData;
	}

	public void setJsonData(TransferJsonData jsonData) {
		this.jsonData = jsonData;
	}

	@Override
	public boolean debugOut() {
		return ! jsonData.getData().getClass().isAnnotationPresent(SkipDebugOut.class)|| ServerConfig.isDebugEnv();
	}

	@Override
	public String _toString() {
		return "=RpcRsp= " + ToString.toString(jsonData.getData())+"]";
	}
}
