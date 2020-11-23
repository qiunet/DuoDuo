package org.qiunet.function.targets;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/***
 * 一个任务. 需要完成多个目标.
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public class Targets {
	private AtomicBoolean finished = new AtomicBoolean();
	private transient TargetContainer container;
	private transient Consumer<Targets> consumer;

	private List<Target> targets;

	private int id;
	public Targets() {}

	/**
	 * 创建一个Targets
	 * @param consumer 配置列表getter
	 * @param consumer 完成通知
	 * @param id 任务的id
	 * @return
	 */
	static Targets valueOf(TargetContainer container,
						   ITargetDefGetter targetDefGetter,
						   Consumer<Targets> consumer,
						   int id) {
		Targets targets0 = new Targets();
		targets0.targets = Lists.newArrayListWithCapacity(targetDefGetter.getTargetList().size());
		targetDefGetter.getTargetList().forEach((index, def) -> targets0.targets.add(Target.valueOf(targetDefGetter, targets0, index)));
		targets0.container = container;
		targets0.consumer = consumer;
		targets0.id = id;
		return targets0;
	}

	/**
	 * 任务是否完成.
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isFinished(){
		return targets.stream().allMatch(Target::isFinished);
	}

	/**
	 * 完成回调.
	 */
	void finishCallback(){
		if (finished.compareAndSet(false, true)) {
			consumer.accept(this);
		}
	}

	/**
	 * 获得container
	 * @return
	 */
	TargetContainer getContainer() {
		return container;
	}

	/**
	 * 循环遍历任务目标
	 * @param targetConsumer 目标消费者
	 */
	public void forEachTarget(Consumer<Target> targetConsumer) {
		this.targets.forEach(targetConsumer);
	}

	public int getId() {
		return id;
	}
}
