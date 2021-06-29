package org.qiunet.function.test.targets;

import io.netty.channel.Channel;
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
	private final TargetContainer<TargetType, PlayerActor> targetContainer;
	private long id;
	private final String name;
	public PlayerActor(long id, String name) {
		super(new DSession((Channel) null));
		this.targetContainer = new TargetContainer<>(this);
		this.name = name;
		this.id = id;
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

	public String getName() {
		return name;
	}

	@Override
	public void auth(long id) {
		this.id = id;
	}
}
