package org.qiunet.function.ai.node.base;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.utils.exceptions.CustomException;

/***
 * action的 基类
 *
 * @author qiunet
 * 2021-07-07 10:37
 */
public abstract class BaseBehaviorAction extends BaseBehaviorNode implements IBehaviorAction {

	@Override
	public ActionStatus run() {
		if (! isRunning()) {
			return super.run();
		}
		ActionStatus status = runningStatusUpdate();
		this.running = status.isRunning();
		return status;
	}

	protected ActionStatus runningStatusUpdate(){
		throw new CustomException("Class [{}] need implements runningStatusUpdate!", getClass().getName());
	}
}
