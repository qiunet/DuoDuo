package org.qiunet.cross.rdc;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.cross.rpc.TransferJsonData;
import org.qiunet.data.util.ServerConfig;
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
@ChannelData(ID = IProtocolId.System.RDC_RSP, desc = "处理处理远程数据调用响应")
public class RouteRdcRsp extends IChannelData {
	/**
	 * 响应端维护自增的id, 需要带着返回给请求服务器.
	 */
	@Protobuf
	private long id;
	@Protobuf
	private TransferJsonData jsonData;

	public static RouteRdcRsp valueOf(long id, IRdcResponse data) {
		RouteRdcRsp response = new RouteRdcRsp();
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

	public IRdcResponse getData() {
		return (IRdcResponse) jsonData.getData();
	}

	@Override
	public boolean debugOut() {
		return ! getData().getClass().isAnnotationPresent(SkipDebugOut.class)|| ServerConfig.isDebugEnv();
	}

	@Override
	public String _toString() {
		return "=RdcRsp= " + ToString.toString(getData())+"]";
	}
}
