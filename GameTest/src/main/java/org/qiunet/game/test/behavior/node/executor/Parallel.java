package org.qiunet.game.test.behavior.node.executor;

import com.google.common.collect.Lists;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorNode;
import org.qiunet.game.test.behavior.node.base.BaseBehaviorExecutor;
import org.qiunet.game.test.robot.Robot;

import java.util.Iterator;
import java.util.List;

/***
 * 并发执行. 根据策略返回成功失败. 默认全部成功. 返回成功.
 *
 * qiunet
 * 2021/8/16 16:40
 **/
public class Parallel extends BaseBehaviorExecutor {
	/**
	 * 所有运行中的节点
	 */
	private final List<IBehaviorNode> runningNodes = Lists.newArrayList();
	/**
	 * 成功数
	 */
	private int successCount;
	/**
	 * 失败数
	 */
	private int failCount;
	/**
	 * 判断成功的策略
	 */
	private final ISuccessPolicy policy;

	public Parallel(IConditions<Robot> preCondition, String name) {
		this(REQUIRE_ALL_SUCCESS, preCondition, name);
	}

	public Parallel(ISuccessPolicy policy, IConditions<Robot> preCondition, String name) {
		super(preCondition, name);
		this.policy = policy;
	}


	@Override
	public void release() {
		this.runningNodes.clear();
		this.successCount = 0;
		this.failCount = 0;
		super.release();
	}

	@Override
	public ActionStatus execute() {
		if (isRunning()) {
			for(Iterator<IBehaviorNode> it = nodes.iterator(); it.hasNext();){
				IBehaviorNode node = it.next();
				ActionStatus status = node.run();
				if(status == ActionStatus.RUNNING){
					continue;
				}
				if (status == ActionStatus.SUCCESS) {
					successCount ++;
				}else {
					failCount ++;
				}
				it.remove();
			}
		} else {
			for (IBehaviorNode node : nodes) {
				ActionStatus status = node.run();
				if (status == ActionStatus.SUCCESS) {
					successCount ++;
				} else if (status == ActionStatus.RUNNING) {
					runningNodes.add(node);
				}else {
					failCount ++;
				}
			}
		}

		if (! runningNodes.isEmpty()) {
			return ActionStatus.RUNNING;
		}
		return this.policy.success(successCount, failCount, nodes.size())  ? ActionStatus.SUCCESS : ActionStatus.FAILURE;
	}

	/**
	 * 判断成功的策略
	 * 如果是RUNNING 状态. 直接返回.
	 */
	@FunctionalInterface
	public interface ISuccessPolicy {
		/**
		 * 是否成功
		 * @param successCount 成功的数量
		 * @param failCount 失败的数量
		 * @param totalCount 总数
		 * @return 是否成功
		 */
		boolean success(int successCount, int failCount, int totalCount);
	}

	/**
	 * 所有必须成功
	 */
	public static final ISuccessPolicy REQUIRE_ALL_SUCCESS = (successCount, failCount, totalCount) -> totalCount == successCount;
	/**
	 * 有一个成功算成功
	 */
	public static final ISuccessPolicy REQUIRE_ONE_SUCCESS = (successCount, failCount, totalCount) -> successCount > 0;
}
