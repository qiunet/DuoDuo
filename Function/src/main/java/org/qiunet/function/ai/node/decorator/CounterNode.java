package org.qiunet.function.ai.node.decorator;

import com.google.common.base.Preconditions;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;

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

	public CounterNode(IBehaviorNode node, int count) {
		super(node);
		Preconditions.checkArgument(count > 0, "count [%s] less than 1!");
		this.count = count;
	}

	@Override
	protected ActionStatus execute() {
		ActionStatus status = node.run();
		if (status == ActionStatus.SUCCESS
		&& currCount.incrementAndGet() >= count) {
			this.parent.removeChild(this);
		}
		return status;
	}
}
