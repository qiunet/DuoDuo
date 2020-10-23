package org.qiunet.cross.actor.auth;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 16:50
 */
@SkipProtoGenerator
@ProtobufClass(description = "跨服用户鉴权请求")
public class CrossPlayerAuthRequest implements IpbRequestData {
	@Protobuf(description = "玩家id")
	private long playerId;

	public static CrossPlayerAuthRequest valueOf(long playerId) {
		CrossPlayerAuthRequest request = new CrossPlayerAuthRequest();
		request.playerId = playerId;
		return request;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
