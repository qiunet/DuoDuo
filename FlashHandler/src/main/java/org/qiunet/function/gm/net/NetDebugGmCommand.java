package org.qiunet.function.gm.net;

import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.data.event.PlayerKickOutEvent;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.offline.UserOfflineManager;
import org.qiunet.flash.handler.common.player.proto.PlayerReLoginPush;
import org.qiunet.flash.handler.common.player.server.UserServerState;
import org.qiunet.flash.handler.common.player.server.UserServerStateManager;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.function.gm.GmCommand;
import org.qiunet.function.gm.GmParamDesc;
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
		TimerManager.executor.scheduleWithDelay(() -> {
			actor.getSession().close(CloseCause.GM_COMMAND);
		},500, TimeUnit.MILLISECONDS);
		return IGameStatus.SUCCESS;
	}

	@GmCommand(commandName = "断开玩家KCP链接")
	public IGameStatus brokenKcp(PlayerActor actor) {
		boolean dSession = actor.getSession() instanceof DSession;
		if (dSession) {
			ISession kcpSession = ((DSession) actor.getSession()).getKcpSession();
			if (kcpSession != null) {
				kcpSession.close(CloseCause.GM_COMMAND);
			}
		}
		return IGameStatus.SUCCESS;
	}

	@GmCommand(commandName = "通知客户端重走完整登录流程")
	public IGameStatus sendReLogin(PlayerActor actor) {
		actor.sendMessage(PlayerReLoginPush.valueOf());
		return IGameStatus.SUCCESS;
	}

	@GmCommand(commandName = "登出玩家")
	private IGameStatus logoutPlayer(PlayerActor player) {
		player.addMessage(AbstractUserActor::logout);
		return IGameStatus.SUCCESS;
	}


	@GmCommand(commandName = "在当前服创建一个指定id的离线玩家对象")
	public IGameStatus touchOfflinePlayer(PlayerActor actor, @GmParamDesc("玩家id(不能在线ID和自己ID):") long playerId) throws InterruptedException {
		// 自己根据返回的GameStatus 判断吧. 没有创建具体的 IGameStatus
		if (actor.getPlayerId() == playerId) {
			return IGameStatus.FAIL;
		}
		UserServerState state = UserServerStateManager.instance.getUserServerState(playerId);
		if (state == null) {
			return IGameStatus.PARAMS_ERROR;
		}

		if (state.isOnline()) {
			return IGameStatus.EXCEPTION;
		}
		if (state.getServerId() > 0) {
			CrossEventManager.fireCrossEvent(state.getServerId(), PlayerKickOutEvent.valueOf(playerId));
			// gm 无所谓
			Thread.sleep(500);
		}

		UserOfflineManager.instance.get(playerId);
		return IGameStatus.SUCCESS;
	}
}
