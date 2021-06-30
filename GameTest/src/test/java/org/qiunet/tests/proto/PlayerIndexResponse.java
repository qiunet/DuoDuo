package org.qiunet.tests.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-09-23 10:22
 */
@PbChannelDataID(value = 1000001)
@ProtobufClass(description = "玩家首页响应")
public class PlayerIndexResponse implements IpbResponseData {

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
