package org.qiunet.function.ai.node.base;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

/***
 * action的 基类
 *
 * @author qiunet
 * 2021-07-07 10:37
 */
public abstract class BaseBehaviorAction<Owner extends MessageHandler<Owner>> extends BaseBehaviorNode<Owner> implements IBehaviorAction<Owner> {

	public BaseBehaviorAction(IConditions<Owner> conditions) {
		super(conditions, StringUtil.EMPTY_STRING);
	}

	@Override
	public ActionStatus run() {
		if (! isRunning()) {
			return super.run();
		}

		ActionStatus status = runningStatusUpdate();
		this.statusLogger.setExecuted(true);
		this.statusLogger.setStatus(status);
		return status;
	}

	protected ActionStatus runningStatusUpdate(){
		throw new CustomException("Class [{}] need implements runningStatusUpdate!", getClass().getName());
	}


	@Override
	public String getName() {
		if (! getClass().isAnnotationPresent(BehaviorAction.class)) {
			return getClass().getSimpleName();
		}
		return getClass().getAnnotation(BehaviorAction.class).name();
	}
}
