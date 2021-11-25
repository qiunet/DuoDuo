package org.qiunet.flash.handler.common.player;

import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.session.DSession;

/***
 * 玩家playerActor 的父类
 *
 * @author qiunet
 * 2020-10-21 10:08
 */
public abstract class AbstractPlayerActor<T extends AbstractPlayerActor<T>>
	extends AbstractUserActor<T> implements ICrossStatusActor, IPlayer, IPlayerDataLoader {

	private PlayerDataLoader dataLoader;
	/**
	 * 玩家ID
	 */
	private long playerId;

	public AbstractPlayerActor(DSession session) {
		super(session);
	}

	public <D extends BasePlayerEventData<T>> void fireEvent(D eventData) {
		super.fireEvent(eventData);
	}

	@Override
	public void auth(long id) {
		if (isAuth()) {
			return;
		}
		this.playerId = id;
		dataLoader = new PlayerDataLoader(id);
		new AuthEventData(this).fireEventHandler();
	}

	@Override
	public void destroy() {
		if (dataLoader != null) {
			dataLoader.destroy();
		}
		super.destroy();
	}

	public long getPlayerId() {
		return playerId;
	}

	@Override
	public long getId() {
		return playerId;
	}

	@Override
	public PlayerDataLoader dataLoader() {
		return dataLoader;
	}
}
