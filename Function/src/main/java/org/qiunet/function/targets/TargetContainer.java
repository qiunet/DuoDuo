package org.qiunet.function.targets;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.utils.args.ArgumentKey;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 16:19
 */
public class TargetContainer<Type extends Enum<Type> & ITargetType, Player extends AbstractPlayerActor<Player>> {
	/**
	 * 保存在player身上的key
	 */
	public static final ArgumentKey<TargetContainer> TARGET_CONTAINER_KEY = new ArgumentKey<>();
	/**
	 * 所有需要监听的任务
	 */
	private Map<Type, List<Target>> targetMap = Maps.newConcurrentMap();
	/**
	 * 写入锁
	 */
	private Lock writeLock;
	/**
	 * 读取锁
	 */
	private Lock readLock;
	/**
	 * 该container持有的玩家id.
	 */
	private Player player;

	/**
	 * 构造一个Container
	 * @param player
	 */
	public TargetContainer(Player player) {
		player.setVal(TARGET_CONTAINER_KEY, this);
		this.player = player;

		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		this.writeLock = lock.writeLock();
		this.readLock = lock.readLock();
	}

	/***
	 * 创建任务的Targets. 并且开启监听
	 * @param targetList 目标的配置列表
	 * @param consumer
	 * @param id
	 */
	public void createAndWatchTargets(TargetDefList targetList, Consumer<Targets> consumer, int id) {
		writeLock.lock();
		try {
			Targets targets = Targets.valueOf(this, targetList, consumer, id);
			targets.forEachTarget(this::watch);
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * 监听目标进度
	 * @param target
	 */
	private void watch(Target target) {
		writeLock.lock();
		try {
			Type targetType = (Type) target.getTargetDef().getTargetType();
			List<Target> targets = targetMap.computeIfAbsent(targetType, key -> new LinkedList<>());
			BaseTargetHandler<Type, Player> handler = TargetHandlerManager.instance.getHandler(targetType);
			targets.add(target);
			// onStartWatch中调用 set 和 addCount 都会调用该方法.
//			target.tryComplete();
			handler.onStartWatch(player, target);
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * 取消监听该任务的进度
	 * @param target
	 */
	void unWatch(Target target) {
		writeLock.lock();
		try {
			Type targetType = (Type) target.getTargetDef().getTargetType();
			List<Target> targets = targetMap.get(targetType);
			if (targets != null) {
				targets.remove(target);
			}
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * 对某个类型的目标进行遍历
	 * @param type
	 * @param consumer
	 */
	void forEatchByType(Type type, Consumer<Target> consumer) {
		readLock.lock();
		try {
			List<Target> targets = targetMap.computeIfAbsent(type, key -> new LinkedList<>());
			targets.forEach(consumer);
		}finally {
			readLock.unlock();
		}
	}
}
