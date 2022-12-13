package org.qiunet.test.function.test.targets;

import org.qiunet.function.targets.BaseTargetHandler;
import org.qiunet.function.targets.ITargetDef;
import org.qiunet.test.function.test.targets.event.KillBossEvent;
import org.qiunet.utils.listener.event.EventListener;

/***
 *
 * @author qiunet
 * 2020-11-23 21:53
 **/
public class KillBossTargetHandler extends BaseTargetHandler<TargetType> {
	@Override
	public TargetType getType() {
		return TargetType.KILL_BOSS;
	}

	@EventListener
	public void killBossEvent(KillBossEvent eventData) {
		this.process(eventData.getPlayer(), (target) -> {
			ITargetDef def = target.getTargetDef();
			if (eventData.getBossId() == Integer.parseInt(def.getTargetParam())) {
				target.addCount();
			}
		});
	}
}
