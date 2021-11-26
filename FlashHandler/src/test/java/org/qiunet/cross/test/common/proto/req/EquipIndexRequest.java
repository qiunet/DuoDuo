package org.qiunet.cross.test.common.proto.req;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.proto.ProtocolId;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:52
 */
@ChannelData(ID = ProtocolId.Equip.EQUIP_INDEX, desc = "装备首页")
public class EquipIndexRequest implements IChannelData {
}
