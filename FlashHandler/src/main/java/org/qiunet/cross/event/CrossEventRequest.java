package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.base.Preconditions;
import org.qiunet.cross.transaction.TransferJsonData;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;
import org.qiunet.utils.listener.event.IEventData;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

/***
 *
 *
 * @author qiunet
 * 2020-10-15 17:00
 */
@SkipProtoGenerator
@ServerCommunicationData
@ChannelData(ID = IProtocolId.System.CROSS_EVENT_REQ, desc = "跨服事件处理")
public class CrossEventRequest implements IChannelData, IDataToString {
	/**
	 * 系统发给玩家的事件. 需要有playerID. 会在玩家的线程执行
	 */
	@Protobuf(description = "玩家ID")
	private long playerId;

	@Protobuf
	private TransferJsonData jsonData;

	public static CrossEventRequest valueOf(IEventData data) {
		return valueOf(data, 0);
	}

	public static CrossEventRequest valueOf(IEventData data, long playerId) {
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

	public IEventData getData() {
		return (IEventData) jsonData.getData();
	}
	@Override
	public String _toString() {
		return "=CrossEvent= "+ ToString.toString(getData());
	}
}
