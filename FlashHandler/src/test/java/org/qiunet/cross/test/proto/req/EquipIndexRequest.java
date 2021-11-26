package org.qiunet.cross.test.proto.req;

import org.qiunet.cross.test.handler.ProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 15:52
 */
@ChannelData(ID = ProtocolId.Equip.EQUIP_INDEX, desc = "装备首页")
public class EquipIndexRequest implements IChannelData {
}
