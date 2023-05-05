package org.qiunet.cross.rpc;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
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
@ChannelData(ID = IProtocolId.System.ROUTE_RPC_REQ, desc = "rpc request")
public class RouteRpcReq extends IChannelData {
	@Protobuf(description = "请求id")
	private int reqId;
	@Protobuf(description = "调用的class")
	private String clz;
	@Protobuf(description = "调用的method")
	private String mtd;
	@Protobuf(description = "真实数据")
	private TransferJsonData data;

	public static RouteRpcReq valueOf(int reqId, String clz, String mtd, IRpcRequest request){
		RouteRpcReq data = new RouteRpcReq();
		data.data = new TransferJsonData(request);
		data.reqId = reqId;
		data.clz = clz;
		data.mtd = mtd;
		return data;
	}

	public int getReqId() {
		return reqId;
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	public String getClz() {
		return clz;
	}

	public void setClz(String clz) {
		this.clz = clz;
	}

	public String getMtd() {
		return mtd;
	}

	public void setMtd(String mtd) {
		this.mtd = mtd;
	}

	public TransferJsonData getData() {
		return data;
	}

	public void setData(TransferJsonData data) {
		this.data = data;
	}

	@Override
	public boolean debugOut() {
		return ! data.getData().getClass().isAnnotationPresent(SkipDebugOut.class);
	}

	@Override
	public String _toString() {
		return "=RpcReq= " + ToString.toString(data.getData())+"]";
	}
}
