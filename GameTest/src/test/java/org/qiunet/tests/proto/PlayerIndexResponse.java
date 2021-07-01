package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import java.util.List;

import static org.qiunet.tests.protocol.ProtocolId.Test.PLAYER_INDEX_RESP;

/***
 *
 *
 * @author qiunet
 * 2020-09-23 10:22
 */
@PbChannelData(ID = PLAYER_INDEX_RESP, desc = "玩家首页响应")
public class PlayerIndexResponse implements IpbChannelData {

	private List<Item> items;

	public static PlayerIndexResponse valueOf(List<Item> items) {
		PlayerIndexResponse response = new PlayerIndexResponse();
		response.items = items;
		return response;
	}

	public List<Item> getItems() {
		return items;
	}
}
