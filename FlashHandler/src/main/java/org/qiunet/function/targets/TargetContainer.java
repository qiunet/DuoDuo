package org.qiunet.function.targets;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.args.ArgumentKey;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/***
 * 任务的容器
 *
 * @author qiunet
 * 2020-11-23 16:19
 */
public class TargetContainer<Type extends Enum<Type> & ITargetType> {
	/**
	 * 保存在player身上的key
	 */
	public static final ArgumentKey<TargetContainer> TARGET_CONTAINER_KEY = new ArgumentKey<>();
	/**
	 * 所有需要监听的任务
	 */
	private final Map<Type, List<Target>> targetMap = Maps.newConcurrentMap();
	/**
	 * 锁
	 */
	private final ReentrantLock lock = new ReentrantLock();
	/**
	 * 该container持有的玩家id.
	 */
	private final PlayerActor player;

	/**
	 * 构造一个Container
	 * @param player
	 */
	public TargetContainer(PlayerActor player) {
		player.setVal(TARGET_CONTAINER_KEY, this);
		this.player = player;
	}

	/***
	 * 创建任务的Targets. 并且开启监听
	 *
	 * @param targetSupply 任务目标提供接口
	 * @param id 任务id
	 */
	public Targets createAndWatchTargets(ITargetSupply targetSupply, int id) {
		return this.createAndWatchTargets(targetSupply.getTargetGetter(id), targetSupply.updateCallback(), id);
	}
	/***
	 * 创建任务的Targets. 并且开启监听
	 *
	 * @param targetDefGetter 目标的配置列表getter
	 * @param updateCallback 更新回调
	 * @param id
	 */
	public Targets createAndWatchTargets(ITargetDefGetter targetDefGetter, BiConsumer<Targets, Target> updateCallback, int id) {
		lock.lock();
		try {
			Targets targets = Targets.valueOf(this, targetDefGetter, updateCallback, id);
			targets.forEachTarget(target -> this.watch(target, true));
			return targets;
		}finally {
			lock.unlock();
		}
	}

	/**
	 * 初始化时候, 从存储的数据库取出来的targets. 添加到容器里面.
	 *
	 * @param targetDefGetter 目标的配置列表getter
	 * @param updateCallback 更新回调.
	 * @param targets 任务集合
	 */
	public void addTargets(ITargetDefGetter targetDefGetter, BiConsumer<Targets, Target> updateCallback, Targets targets) {
		lock.lock();
		try {
			targets.updateCallback = updateCallback;
			targets.container = this;
			targets.forEachTarget(target -> {
				target.targetDef = targetDefGetter.getTargetDef(target.getIndex());
				target.targets = targets;
			});

			targets.forEachTarget(target -> {
				if (target.isFinished()) {
					updateCallback.accept(targets, target);
					return;
				}
				this.watch(target, false);
			});

		}finally {
			lock.unlock();
		}
	}

	/**
	 * 监听目标进度
	 * @param target
	 */
	private void watch(Target target, boolean needInit) {
		lock.lock();
		try {
			Type targetType = target.getTargetDef().getTargetType();
			List<Target> targets = targetMap.computeIfAbsent(targetType, key -> new LinkedList<>());
			BaseTargetHandler<Type> handler = TargetHandlerManager.instance.getHandler(targetType);
			targets.add(target);
			if (needInit) {
				// onStartWatch中调用 set 和 addCount 都会调用该方法.
				// target.tryComplete();
				handler.onStartWatch(player, target);
			}
		}finally {
			lock.unlock();
		}
	}

	/**
	 * 取消监听该任务的进度
	 * @param target
	 */
	void unWatch(Target target) {
		lock.lock();
		try {
			Type targetType = target.getTargetDef().getTargetType();
			List<Target> targets = targetMap.get(targetType);
			if (targets != null) {
				targets.remove(target);
			}
		}finally {
			lock.unlock();
		}
	}

	/**
	 * 对某个类型的目标进行遍历
	 * @param type
	 * @param consumer
	 */
	void forEachByType(Type type, Consumer<Target> consumer) {
		lock.lock();
		try {
			List<Target> targets = targetMap.computeIfAbsent(type, key -> new LinkedList<>());
			for (Target target : targets) {
				consumer.accept(target);
			}
		}finally {
			lock.unlock();
		}
	}
}
