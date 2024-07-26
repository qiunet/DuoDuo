package org.qiunet.cross.rdc;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.cross.rpc.TransferJsonData;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.string.ToString;

/***
 * 传输过程的信息.
 *
 * @author qiunet
 * 2020-09-24 09:34
 */
@SkipProtoGenerator
@ServerCommunicationData
@ChannelData(ID = IProtocolId.System.RDC_REQ, desc = "处理远程数据调用的请求")
public class RouteRdcReq extends IChannelData {
	/**
	 * 请求端维护自增的id, 需要带着返回给请求服务器.
	 */
	@Protobuf
	private long id;
	@Protobuf
	private TransferJsonData jsonData;


	static RouteRdcReq valueOf(long id, IRdcRequest data) {
		RouteRdcReq request = new RouteRdcReq();
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

	public IRdcRequest getData() {
		return (IRdcRequest) jsonData.getData();
	}

	@Override
	public boolean debugOut() {
		return SkipDebugOut.DebugOut.test(getData().getClass());
	}

	@Override
	public String _toString() {
		return "=RdcReq= " + ToString.toString(getData())+"]";
	}
}
