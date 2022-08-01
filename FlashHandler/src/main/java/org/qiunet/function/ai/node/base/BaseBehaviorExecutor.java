package org.qiunet.function.ai.node.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.observer.IBHTAddNodeObserver;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.reflect.ReflectUtil;

import java.util.List;

/***
 * 行为树的执行容器
 *
 * @author qiunet
 * 2021-07-08 10:50
 */
public abstract class BaseBehaviorExecutor<Owner extends MessageHandler<Owner>> extends BaseBehaviorNode<Owner> implements IBehaviorExecutor<Owner> {
	/**
	 * 节点内所有的Node
	 */
	private final List<IBehaviorNode<Owner>> nodes = Lists.newArrayList();


	public BaseBehaviorExecutor(IConditions<Owner> conditions, String name) {
		super(conditions, name);
	}

	@Override
	public int childSize() {
		return nodes.size();
	}

	/**
	 * 添加action
	 * @param nodes
	 */
	@SafeVarargs
	@Override
	public final IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... nodes) {
		for (IBehaviorNode<Owner> node : nodes) {
			ReflectUtil.setField(node, "parent", this);
			if (this.rootNode() != null) {
				this.rootNode().syncFireObserver(IBHTAddNodeObserver.class, o -> o.addNode(node));
			}else {
				this.rootNode.addCompleteListener((n) -> {
					n.syncFireObserver(IBHTAddNodeObserver.class, o -> o.addNode(node));
				});
			}
			this.nodes.add(node);
		}
		return this;
	}

	@Override
	public List<IBehaviorNode<Owner>> getChildNodes() {
		return ImmutableList.copyOf(nodes);
	}
}
