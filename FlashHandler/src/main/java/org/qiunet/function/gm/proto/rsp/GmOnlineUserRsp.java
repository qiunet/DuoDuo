package org.qiunet.function.gm.proto.rsp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;

import java.util.List;

import static org.qiunet.flash.handler.common.id.IProtocolId.System.GM_ONLINE_USER_RSP;

/***
 *
 * @author qiunet
 * 2022/3/7 11:21
 */
@SkipProtoGenerator
@ChannelData(ID = GM_ONLINE_USER_RSP, desc = "在线玩家响应")
public class GmOnlineUserRsp extends IChannelData {
	@Protobuf(description = "用户列表")
	private List<OnlineUserInfo> userList;

	public static GmOnlineUserRsp valueOf(List<OnlineUserInfo> userList) {
		GmOnlineUserRsp data = new GmOnlineUserRsp();
		data.userList = userList;
		return data;
	}

	public List<OnlineUserInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<OnlineUserInfo> userList) {
		this.userList = userList;
	}
}
