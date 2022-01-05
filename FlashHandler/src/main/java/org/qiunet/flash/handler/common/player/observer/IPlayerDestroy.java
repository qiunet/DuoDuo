package org.qiunet.flash.handler.common.player.observer;

import org.qiunet.flash.handler.common.observer.IObserver;
import org.qiunet.flash.handler.common.player.AbstractUserActor;

/***
 * 销毁PlayerActor对象
 *
 * 玩家登出. 或者 掉线后, 一定时间回来(失去重连资格).
 * 触发该事件
 *
 * qiunet
 * 2021/9/26 14:44
 **/
public interface IPlayerDestroy extends IObserver {

	void destroyActor(AbstractUserActor actor);
}
