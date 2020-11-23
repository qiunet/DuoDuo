package org.qiunet.function.test.targets;

import org.qiunet.function.targets.BaseTargetHandler;
import org.qiunet.function.test.targets.event.KillBossEventData;
import org.qiunet.listener.event.EventListener;

/***
 *
 * @author qiunet
 * 2020-11-23 21:53
 **/
public class KillBossTargetHandler extends BaseTargetHandler<TargetType, PlayerActor> {
	@Override
	public TargetType getType() {
		return TargetType.KILL_BOSS;
	}

	@EventListener
	public void killBossEvent(KillBossEventData eventData) {
		this.process(eventData.getPlayer(), (target, def) -> {
			if (eventData.getBossId() == Integer.parseInt(def.getTargetParam())) {
				target.addCount();
			}
		});
	}
}
