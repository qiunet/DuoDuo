package org.qiunet.test.function.test.targets;

import org.qiunet.function.targets.BaseTargetHandler;
import org.qiunet.test.function.test.targets.event.LevelUpEventData;
import org.qiunet.utils.listener.event.EventListener;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:49
 */
public class LevelTargetHandler extends BaseTargetHandler<TargetType> {

	@EventListener
	public void handlerLevelUp(LevelUpEventData eventData) {
		process(eventData.getPlayer(), (target, def) -> target.alterToCount(eventData.getLevel()));
	}

	@Override
	public TargetType getType() {
		return TargetType.LEVEL;
	}
}
