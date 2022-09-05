package org.qiunet.function.targets;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.scanner.anno.AutoWired;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
	private static final ArgumentKey<TargetContainer<?>> TARGET_CONTAINER_KEY = new ArgumentKey<>();
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

	public static <Type extends Enum<Type> & ITargetType> TargetContainer<Type> get(PlayerActor actor) {
		return (TargetContainer<Type>) actor.computeIfAbsent(TARGET_CONTAINER_KEY, () -> new TargetContainer<>(actor));
	}

	/**
	 * 构造一个Container
	 * @param player
	 */
	private TargetContainer(PlayerActor player) {
		this.player = player;
	}

	/***
	 * 创建任务的Targets. 并且开启监听
	 *
	 * @param targetsDefGetter 目标的配置列表getter
	 * @param updateCallback 更新回调
	 * @param id
	 */
	public Targets createAndWatchTargets(ITargetsDefGetter targetsDefGetter, BiConsumer<Targets, Target> updateCallback, int id) {
		lock.lock();
		try {
			Targets targets = Targets.valueOf(this, targetsDefGetter, updateCallback, id);
			targets.forEachTarget(target -> this.watch(target, true));
			return targets;
		}finally {
			lock.unlock();
		}
	}

	/**
	 * 初始化时候, 从存储的数据库取出来的targets. 添加到容器里面.
	 *
	 * @param updateCallback 更新回调.
	 * @param targets 任务集合
	 */
	public void addTargets(ITargetDefGetter getter, BiConsumer<Targets, Target> updateCallback, Targets targets) {
		lock.lock();
		try {
			targets.updateCallback = updateCallback;
			targets.container = this;
			targets.forEachTarget(target -> {
				target.targetDef = getter.getTargetDef(target.getTid());
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
	 * 获得玩家对象
	 * @return actor
	 */
	public PlayerActor getPlayer() {
		return player;
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

	/**
	 * 清理所有的任务
	 */
	public void clear() {
		this.targetMap.clear();
	}
}
