package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.observer.IObserverSupportOwner;
import org.qiunet.flash.handler.common.observer.ObserverSupport;
import org.qiunet.flash.handler.common.player.event.UserEventData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.utils.listener.event.EventManager;

/***
 * 玩家类型的messageActor 继承该类
 *
 * @author qiunet
 * 2020-10-13 20:51
 */
public abstract class AbstractUserActor<T extends AbstractUserActor<T>> extends AbstractMessageActor<T> implements IObserverSupportOwner<T>, IPlayer {
	/**
	 * 观察者
	 */
	private final ObserverSupport<T> observerSupport = new ObserverSupport<>((T)this);

	public AbstractUserActor(DSession session) {
		super(session);
	}

	@Override
	protected void setSession(DSession session) {
		this.session = session;
	}

	@Override
	public ObserverSupport<T> getObserverSupport() {
		return observerSupport;
	}

	/**
	 * 对事件的处理.
	 * 跨服的提交跨服那边. 本服调用自己的
	 * @param eventData
	 */
	public <D extends UserEventData> void fireEvent(D eventData){
		EventManager.fireEventHandler(eventData.setPlayer(this));
	}

	/**
	 * 对事件的处理.
	 * 跨服的提交跨服那边. 本服调用自己的
	 * @param eventData
	 */
	public <D extends UserEventData> void fireAsyncEvent(D eventData){
		this.addMessage(p -> {
			EventManager.fireEventHandler(eventData.setPlayer(p));
		});
	}

	/**
	 * 是否是跨服对象
	 * @return
	 */
	public abstract boolean isCrossPlayer();

	/**
	 * 获得session
	 * @return
	 */
	public DSession getSession(){
		return session;
	}

	/**
	 * 是player Actor
	 * @return
	 */
	public boolean isPlayerActor() {
		return ! isCrossPlayer();
	}

	/**
	 * 玩家主动退出
	 */
	public void logout() {
		UserOnlineManager.instance.playerQuit((T)this);
	}

	@Override
	public void destroy() {
		super.destroy();
		observerSupport.clear();
	}
}
