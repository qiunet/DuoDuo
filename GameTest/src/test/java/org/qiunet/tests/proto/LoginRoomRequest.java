package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.ROOM_LOGIN_REQ;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:59
 */
@PbChannelData(ID = ROOM_LOGIN_REQ, desc = "房间服的登录")
public class LoginRoomRequest implements IpbChannelData {

}
