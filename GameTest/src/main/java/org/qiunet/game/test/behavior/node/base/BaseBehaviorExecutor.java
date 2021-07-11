package org.qiunet.game.test.behavior.node.base;

import com.google.common.collect.Lists;
import org.qiunet.game.test.behavior.action.IBehaviorExecutor;
import org.qiunet.game.test.behavior.action.IBehaviorNode;
import org.qiunet.game.test.robot.Robot;

import java.util.List;

/***
 * 行为树的执行容器
 *
 * @author qiunet
 * 2021-07-08 10:50
 */
public abstract class BaseBehaviorExecutor extends BaseNode implements IBehaviorExecutor {
	/**
	 * 节点内所有的action
	 */
	protected final List<IBehaviorNode> actions = Lists.newArrayList();
	/**
	 * 执行器的名称.
	 * 比如登入游戏  升级装备 等
	 */
	protected String name;
	public BaseBehaviorExecutor(Robot robot, String name) {
		super(robot);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 添加action
	 * @param actions
	 */
	public void addAction(IBehaviorNode ... actions) {
		for (IBehaviorNode action : actions) {
			((BaseNode) action).parent = this;
			this.actions.add(action);
		}
	}
}
