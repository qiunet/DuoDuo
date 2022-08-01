package org.qiunet.function.ai.observer;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.observer.IObserver;
import org.qiunet.function.ai.node.IBehaviorNode;


/***
 * 添加节点事件
 *
 * @author qiunet
 * 2022/8/3 22:06
 */
public interface IBHTAddNodeObserver<Owner extends MessageHandler<Owner>> extends IObserver {

	void addNode(IBehaviorNode<Owner> node);
}
