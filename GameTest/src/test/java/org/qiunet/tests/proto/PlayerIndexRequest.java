package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.PLAYER_INDEX_REQ;

/***
 *
 *
 * @author qiunet
 * 2020-09-23 10:18
 */
@PbChannelData(ID = PLAYER_INDEX_REQ, desc = "长连接首页")
public class PlayerIndexRequest implements IpbChannelData {

}
