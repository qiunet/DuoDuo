package org.qiunet.game.test.behavior.node.decorator;

import com.google.common.base.Preconditions;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.behavior.node.IBehaviorAction;
import org.qiunet.game.test.behavior.node.base.BaseDecorator;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * 计数节点. 如果完成次数达到指定数量. 就从父类删除
 * 比如登录. 生命周期只需要一次.
 * qiunet
 * 2021/8/16 21:40
 **/
public class CounterNode extends BaseDecorator {
	private final AtomicInteger currCount = new AtomicInteger();
	private final int count;

	public CounterNode(IBehaviorAction action, int count) {
		super(action);
		Preconditions.checkArgument(count > 0, "count [%s] less than 1!");
		this.count = count;
	}

	@Override
	protected ActionStatus execute() {
		ActionStatus status = action.run();
		if (status == ActionStatus.SUCCESS
		&& currCount.incrementAndGet() >= count) {
			this.parent.removeChild(this);
		}
		return status;
	}
}
