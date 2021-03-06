package org.qiunet.function.targets;

import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.utils.args.Argument;

import java.util.function.BiConsumer;

/***
 * 目标处理基础类
 *
 * @author qiunet
 * 2020-11-23 17:10
 */
public abstract class BaseTargetHandler<Type extends Enum<Type> & ITargetType, Player extends AbstractPlayerActor<Player>> {
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
	protected void process(Player player, BiConsumer<Target, ITargetDef> consumer) {
		Argument<TargetContainer> argument = player.getArgument(TargetContainer.TARGET_CONTAINER_KEY);
		TargetContainer<Type, Player> targetContainer = argument.get();
		targetContainer.forEachByType(getType(), consumer);
	}

	/**
	 * 开始监听时候. 如果需要.覆盖该方法.
	 * 一般比如target开始已经有值了. 需要set到target里面去
	 * @param player
	 * @param target
	 */
	void onStartWatch(Player player, Target target){
		// do nothing
	}
}
