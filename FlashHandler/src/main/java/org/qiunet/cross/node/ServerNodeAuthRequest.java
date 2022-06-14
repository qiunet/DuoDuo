package org.qiunet.cross.node;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.secret.MD5Util;

/***
 *
 * serverNode  鉴权请求
 *
 * @author qiunet
 * 2020-10-22 15:57
 */
@SkipProtoGenerator
@ServerCommunicationData
@ChannelData(ID = IProtocolId.System.SERVER_NODE_AUTH, desc = "serverNode 鉴权请求")
public class ServerNodeAuthRequest implements IChannelData {
	@Protobuf(description = "请求serverId 鉴权")
	private int serverId;
	@Protobuf(description = "事件戳 秒")
	private long dt;
	@Protobuf(description = "签名")
	private String sign;

	public static ServerNodeAuthRequest valueOf(int serverId) {
		ServerNodeAuthRequest request = new ServerNodeAuthRequest();
		request.dt = DateUtil.currSeconds();
		request.sign = makeSign(request.dt);
		request.serverId = serverId;
		return request;
	}

	/**
	 * 构造签名
	 * @param dt
	 * @return
	 */
	public static String makeSign(long dt) {
		return MD5Util.encrypt(ServerConfig.getSecretKey() + dt);
	}

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}
