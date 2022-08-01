package org.qiunet.function.ai.node.executor;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseBehaviorExecutor;
import org.qiunet.function.condition.IConditions;

import java.util.Iterator;
import java.util.List;

/***
 * 并发执行. 根据策略返回成功失败. 默认全部成功. 返回成功.
 *
 * qiunet
 * 2021/8/16 16:40
 **/
public class ParallelExecutor<Owner extends MessageHandler<Owner>> extends BaseBehaviorExecutor<Owner> {
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

	public ParallelExecutor(IConditions<Owner> conditions) {
		this(conditions, REQUIRE_ALL_SUCCESS, "并行执行");
	}

	public ParallelExecutor(IConditions<Owner> conditions, String name) {
		this(conditions, REQUIRE_ALL_SUCCESS, name);
	}

	public ParallelExecutor(IConditions<Owner> conditions, ISuccessPolicy policy, String name) {
		super(conditions, name);
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
			for(Iterator<IBehaviorNode> it = runningNodes.iterator(); it.hasNext();){
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
			for (IBehaviorNode node : this.getChildNodes()) {
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
		return this.policy.success(successCount, failCount, childSize())  ? ActionStatus.SUCCESS : ActionStatus.FAILURE;
	}

	@Override
	public void reset() {
		super.reset();
		this.failCount = 0;
		this.successCount = 0;
		this.runningNodes.clear();
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
