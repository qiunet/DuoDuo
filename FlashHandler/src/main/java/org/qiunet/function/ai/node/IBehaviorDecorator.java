package org.qiunet.function.ai.node;

import org.qiunet.flash.handler.common.MessageHandler;

/***
 * 节点装饰类接口.
 *
 * @author qiunet
 * 2021-07-05 09:57
 */
public interface IBehaviorDecorator<Owner extends MessageHandler<Owner>> extends IBehaviorExecutor<Owner> {

	/**
	 * 获得装饰节点包含的节点
	 * @return
	 */
	IBehaviorNode<Owner> getNode();

	@Override
	default int childSize() {
		return 1;
	}
}
