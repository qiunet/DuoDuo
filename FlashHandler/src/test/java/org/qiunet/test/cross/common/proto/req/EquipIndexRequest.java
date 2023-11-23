package org.qiunet.test.cross.common.proto.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.player.protocol.IgnoreCommonProtocolCDCheck;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.test.handler.proto.ProtocolId;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:52
 */
@IgnoreCommonProtocolCDCheck
@ChannelData(ID = ProtocolId.Equip.EQUIP_INDEX, desc = "装备首页")
public class EquipIndexRequest extends IChannelData {
	@Protobuf
	private int val;

	public static EquipIndexRequest valueOf(int val){
		EquipIndexRequest data = new EquipIndexRequest();
	    data.val = val;
		return data;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
}
