package org.qiunet.function.test.targets;

import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.function.targets.TargetContainer;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:35
 */
public class PlayerActor extends AbstractPlayerActor<PlayerActor> {
	private TargetContainer<TargetType, PlayerActor> targetContainer;
	private long id;
	public PlayerActor(DSession session) {
		super(session);
		this.targetContainer = new TargetContainer<>(this);
	}

	public TargetContainer<TargetType, PlayerActor> getTargetContainer() {
		return targetContainer;
	}

	@Override
	public boolean isCrossStatus() {
		return false;
	}

	@Override
	public DSession crossSession() {
		return null;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void auth(long id) {
		this.id = id;
	}
}
