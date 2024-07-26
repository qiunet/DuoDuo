package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.base.Preconditions;
import org.qiunet.cross.rpc.TransferJsonData;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.protocol.IgnoreCommonProtocolCDCheck;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.listener.event.IListenerEvent;
import org.qiunet.utils.string.ToString;

/***
 *
 *
 * @author qiunet
 * 2020-10-15 17:00
 */
@SkipProtoGenerator
@ServerCommunicationData
@IgnoreCommonProtocolCDCheck
@ChannelData(ID = IProtocolId.System.CROSS_EVENT_REQ, desc = "跨服事件处理")
public class CrossEventRequest extends IChannelData {
	/**
	 * 系统发给玩家的事件. 需要有playerID. 会在玩家的线程执行
	 */
	@Protobuf(description = "玩家ID")
	private long playerId;

	@Protobuf
	private TransferJsonData jsonData;

	public static CrossEventRequest valueOf(IListenerEvent data) {
		return valueOf(data, 0);
	}

	public static CrossEventRequest valueOf(IListenerEvent data, long playerId) {
		Preconditions.checkNotNull(data);

		CrossEventRequest request = new CrossEventRequest();
		request.jsonData = new TransferJsonData(data);
		request.playerId = playerId;
		return request;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public TransferJsonData getJsonData() {
		return jsonData;
	}

	public void setJsonData(TransferJsonData jsonData) {
		this.jsonData = jsonData;
	}

	public IListenerEvent getData() {
		return (IListenerEvent) jsonData.getData();
	}

	@Override
	public boolean debugOut() {
		return SkipDebugOut.DebugOut.test(getData().getClass());
	}

	@Override
	public String _toString() {
		return "=CrossEvent= "+ ToString.toString(getData());
	}
}
