package org.qiunet.function.targets;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.args.Argument;

import java.util.function.Consumer;

/***
 * 目标处理基础类
 *
 * @author qiunet
 * 2020-11-23 17:10
 */
public abstract class BaseTargetHandler<Type extends Enum<Type> & ITargetType> {
	/**
	 * 获得类型
	 * @return
	 */
	public abstract Type getType();
	/**
	 * 处理进度
	 * @param player 玩家
	 * @param consumer target
	 */
	protected void process(PlayerActor player, Consumer<Target> consumer) {
		this.process(player, consumer, null);
	}

	/**
	 * 处理进度
	 * @param player 玩家
	 * @param consumer target
	 * @param after 后执行
	 */
	protected void process(PlayerActor player, Consumer<Target> consumer, Runnable after) {
		TargetContainer<Type> targetContainer = TargetContainer.get(player);
		if (targetContainer == null) {
			return;
		}
		targetContainer.forEachByType(getType(), consumer);
		if (after != null) {
			after.run();
		}
	}

	/**
	 * 开始监听时候. 如果需要.覆盖该方法.
	 * 一般比如target开始已经有值了. 需要set到target里面去
	 * @param player
	 * @param target
	 */
	protected void onStartWatch(PlayerActor player, Target target){
		// do nothing
	}
}
