package org.qiunet.function.ai.node.decorator;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;
import org.qiunet.utils.string.StringUtil;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * 循环次数的包装器
 * 只有到达循环次数才返回success,
 * 如果子类是失败了. 也返回失败.
 *
 * @author qiunet
 * 2022/2/21 21:24
 */
public class RepeatNode<Owner extends MessageHandler<Owner>> extends BaseDecorator<Owner> {
	/**
	 * 当前执行次数
	 */
	private final AtomicInteger currCount = new AtomicInteger();
	/**
	 * 需要循环的次数
	 */
	private final int count;

	public RepeatNode(IBehaviorNode<Owner> node, int count) {
		this(node, count, "");
	}

	public RepeatNode(IBehaviorNode<Owner> node, int count, String name) {
		this(name, count);
		this.addChild(node);
	}

	public RepeatNode(String name, int count) {
		super(StringUtil.isEmpty(name) ? "Repeat": name);
		Preconditions.checkArgument(count > 0, "count [%s] less than 1!");
		this.count = count;
	}

	@Override
	public void release() {
		super.release();
		currCount.set(0);
	}

	@Override
	protected ActionStatus execute() {
		ActionStatus status = node.run();
		if (status == ActionStatus.SUCCESS) {
			currCount.incrementAndGet();
			return currCount.get() >= count ? ActionStatus.SUCCESS : ActionStatus.RUNNING;
		}
		return status;
	}
}
