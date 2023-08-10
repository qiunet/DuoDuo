package org.qiunet.function.gm.net;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.function.gm.GmCommand;
import org.qiunet.utils.timer.TimerManager;

import java.util.concurrent.TimeUnit;

/***
 * 网络debug gm工具
 * @author qiunet
 * 2023/7/25 20:53
 */
enum NetDebugGmCommand {
	instance;

	@GmCommand(commandName = "断开玩家TCP/WS链接")
	public IGameStatus brokenTcp(PlayerActor actor) {
		TimerManager.instance.scheduleWithDelay(() -> {
			actor.getSession().close(CloseCause.GM_COMMAND);
		},500, TimeUnit.MILLISECONDS);
		return IGameStatus.SUCCESS;
	}

	@GmCommand(commandName = "断开玩家KCP链接")
	public IGameStatus brokenKcp(PlayerActor actor) {
		ISession kcpSession = actor.getSession().getKcpSession();
		if (kcpSession != null) {
			kcpSession.close(CloseCause.GM_COMMAND);
		}
		return IGameStatus.SUCCESS;
	}
}
