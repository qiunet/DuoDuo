package org.qiunet.cross.actor.auth;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 16:50
 */
@SkipProtoGenerator
@ServerCommunicationData
@ChannelData(ID = IProtocolId.System.CROSS_PLAYER_AUTH, desc = "跨服鉴权")
public class CrossPlayerAuthRequest extends IChannelData {
	@Protobuf(description = "玩家id")
	private long playerId;
	@Protobuf(description = "玩家的serverId")
	private int serverId;
	@Protobuf(description = "cross使用的msgQueueIndex")
	private String crossMsgQueueIndex;

	public static CrossPlayerAuthRequest valueOf(long playerId, int serverId, String crossMsgQueueIndex) {
		Preconditions.checkArgument(playerId > 0 && !Strings.isNullOrEmpty(crossMsgQueueIndex));
		CrossPlayerAuthRequest request = new CrossPlayerAuthRequest();
		request.crossMsgQueueIndex = crossMsgQueueIndex;
		request.playerId = playerId;
		request.serverId = serverId;
		return request;
	}

	public String getCrossMsgQueueIndex() {
		return crossMsgQueueIndex;
	}

	public int getServerId() {
		return serverId;
	}

	public long getPlayerId() {
		return playerId;
	}
}
