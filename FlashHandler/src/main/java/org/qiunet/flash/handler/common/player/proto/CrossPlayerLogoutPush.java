package org.qiunet.flash.handler.common.player.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2021/12/6 11:19
 */
@ChannelData(ID = IProtocolId.System.CROSS_PLAYER_NEED_LOGOUT_PUSH, desc = "玩家需要退出当前跨服玩法, 当前服务器要关了!")
public class CrossPlayerLogoutPush extends IChannelData {
	@Ignore
	public static final CrossPlayerLogoutPush instance = new CrossPlayerLogoutPush();
}
