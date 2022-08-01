package org.qiunet.function.ai.log;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.utils.string.StringUtil;

/***
 * 行为树每次tick的日志
 *
 * @author qiunet
 * 2022/7/28 14:56
 */
public class BHTStatusLogger<Owner extends MessageHandler<Owner>> {
	/**
	 * 节点
	 */
	private final IBehaviorNode<Owner> node;
	/**
	 * 是否执行
	 */
	private boolean executed;
	/**
	 * 返回状态
	 */
	private ActionStatus status;

	public BHTStatusLogger(IBehaviorNode<Owner> node) {
		this.node = node;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public void setStatus(ActionStatus status) {
		this.status = status;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void recycle() {
		this.executed = false;
		this.status = null;
	}

	public IBehaviorNode<?> getNode() {
		return node;
	}

	public ActionStatus getStatus() {
		return status;
	}

	public String log(int tabCount) {
		StringBuilder builder = new StringBuilder();
		builder.append(StringUtil.repeated("\t", tabCount));
		builder.append(node instanceof IBehaviorAction ? ". ": "- ");

		builder.append(node.getClass().getSimpleName()).append("(").append(node.getName()).append(") ");
		if (! executed) {
			builder.append("未执行");
		}else {
			builder.append("状态:").append(this.status);
		}
		return builder.append("\n").toString();
	}
}
