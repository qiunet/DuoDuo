package org.qiunet.function.ai.node.executor;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.condition.IConditions;

/***
 * Root 执行器
 *
 * @author qiunet
 * 2022-07-28 10:39
 */
public class RootExecutor<Owner extends MessageHandler<Owner>> extends SelectorExecutor<Owner> {
	/**
     * 当前执行索引
	 */
	private int currIndex;

	public RootExecutor(IConditions<Owner> conditions) {
		this(conditions, "ROOT");
	}

	public RootExecutor(IConditions<Owner> conditions, String name) {
		super(conditions, name, false);
	}
}
