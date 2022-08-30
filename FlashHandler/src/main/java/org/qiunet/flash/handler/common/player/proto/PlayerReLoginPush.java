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
@ChannelData(ID = IProtocolId.System.PLAYER_RE_LOGIN_PUSH, desc = "玩家重新去登录通知.当前服务器要关闭!")
public class PlayerReLoginPush extends IChannelData {
	@Ignore
	private static final PlayerReLoginPush instance = new PlayerReLoginPush();

	public static PlayerReLoginPush valueOf(){
		return instance;
	}
}
