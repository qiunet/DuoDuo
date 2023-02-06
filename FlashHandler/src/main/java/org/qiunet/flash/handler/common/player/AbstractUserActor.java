package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.observer.IObserverSupportOwner;
import org.qiunet.flash.handler.common.observer.ObserverSupport;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 玩家类型的messageActor 继承该类
 *
 * @author qiunet
 * 2020-10-13 20:51
 */
public abstract class AbstractUserActor<T extends AbstractUserActor<T>>
		extends AbstractMessageActor<T> implements IObserverSupportOwner<T>, IPlayer {
	/**
	 * 观察者
	 */
	private final ObserverSupport<T> observerSupport = new ObserverSupport<>((T)this);

	public AbstractUserActor(ISession session) {
		super(session);
	}

	@Override
	protected void setSession(ISession session) {
		this.session = session;
	}

	@Override
	public ObserverSupport<T> getObserverSupport() {
		return observerSupport;
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
	public ISession getSession(){
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
		this.getSession().close(CloseCause.LOGOUT);
	}

	@Override
	public void destroy() {
		super.destroy();
		observerSupport.clear();
	}
}
