package org.qiunet.cross.test.proto.resp;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.cross.test.handler.ProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 12:39
 */
@ProtobufClass(description = "跨服登录成功")
@PbResponse(ProtocolId.Player.CROSS_PLAYER_LOGIN_SUCCESS)
public class CrossLoginResponse implements IpbResponseData {
}
