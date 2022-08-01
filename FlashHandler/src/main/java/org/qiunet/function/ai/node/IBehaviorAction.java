package org.qiunet.function.ai.node;


import org.qiunet.flash.handler.common.MessageHandler;

/***
 * 具体的测试动作.
 * 执行一个测试动作.
 * Obj 为执行行为树的对象, 比如 robot .
 * 需要提供构造函数. constructor(Obj, Conditions)
 *
 * @author qiunet
 * 2021-07-05 09:57
 */
public interface IBehaviorAction<Owner extends MessageHandler<Owner>> extends IBehaviorNode<Owner> {
}
