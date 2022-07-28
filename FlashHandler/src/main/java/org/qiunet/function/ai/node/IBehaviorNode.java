package org.qiunet.function.ai.node;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.utils.math.IWeightObj;

/***
 * 行为节点.
 * 行为节点一般有 进入 退出  cancel方法, 客户端对ai用来进行动作前后的一些行为处理 或者打断.
 * 但是我们是纯服务器. 所以可以没有.
 *
 * @author qiunet
 * 2021-07-05 09:57
 */
public interface IBehaviorNode<Owner> extends IWeightObj {
	/**
	 * 自动分配的id
	 * @return
	 */
	int getId();
	/**
	 * 重置
	 */
	default void reset(){}
	/**
	 * 构造后的检查
	 */
	default void check() {}
	/**
	 * root 构造 后的准备工作.
	 *
	 * 只执行一次
	 */
	default void prepare() {}
	/**
	 * 每次重新执行前 都会初始化
	 */
	default void initialize(){}
	/**
	 * 结束
	 */
	default void release(){}
	/**
	 * 运行节点.
	 * @return 成功 或者 失败
	 */
	ActionStatus run();
	/**
	 * 名称
	 * @return
	 */
	String getName();

	void setName(String name);
	/**
	 *
	 * @return
	 */
	default Owner getOwner() {
		return rootNode().getOwner();
	}

	/**
	 * 根节点
	 * @return
	 */
	BehaviorRootTree<Owner> rootNode();
	/**
	 * 是否运行中.
	 * @return
	 */
	boolean isRunning();
	/**
	 * 前置条件
	 * @return
	 */
	boolean preCondition();
	/**
	 * 得到父节点
	 * @return
	 */
	IBehaviorExecutor<Owner> parent();
	/**
	 * 权重.
	 * 在 RANDOM 控制器下. 可以按照权重来.
	 * @return 子类覆盖实现
	 */
	default int weight(){
		return 1;
	}
}
