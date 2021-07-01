package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.ONLINE_LOGIN_REQ;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:49
 */
@PbChannelData(ID = ONLINE_LOGIN_REQ, desc = "登录Online服务")
public class LoginOnlineRequest implements IpbChannelData {
}
