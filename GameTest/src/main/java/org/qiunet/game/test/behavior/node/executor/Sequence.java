package org.qiunet.game.test.behavior.node.executor;

import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.behavior.node.base.BaseBehaviorExecutor;
import org.qiunet.utils.exceptions.CustomException;

/***
 * 顺序执行容器 (与行为)
 *
 * 如果返回 failure. 则停止执行 返回 failure
 *
 * @author qiunet
 * 2021-07-07 10:39
 */
public class Sequence extends BaseBehaviorExecutor<Sequence> {
	/**
	 * 当前执行
	 */
	private int currIndex;


	@Override
	public boolean preCondition() {
		for (IBehaviorNode node : nodes) {
			if (! node.preCondition()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initialize() {
		if (nodes.isEmpty()) {
			throw new CustomException("Class [{}] child nodes is empty!", getClass().getName());
		}
		this.currIndex = 0;
	}

	@Override
	public ActionStatus execute() {
		for (; currIndex < nodes.size(); currIndex++) {
			this.currentBehavior = nodes.get(currIndex);
			if (! this.currentBehavior.preCondition()) {
				return ActionStatus.FAILURE;
			}

			ActionStatus status = this.currentBehavior.run();
			if (status != ActionStatus.SUCCESS) {
				return status;
			}

			if (currIndex < nodes.size() - 1) {
				try {
					Thread.sleep(EVERY_NODE_SLEEP_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return ActionStatus.SUCCESS;
	}
}
