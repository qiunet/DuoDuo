package org.qiunet.function.targets;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/***
 * 一个任务. 需要完成多个目标.
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public class Targets {
	/**
	 * 更新回调
	 */
	transient BiConsumer<Targets, Target> updateCallback;
	/**
	 * 目标容器
	 */
	transient TargetContainer container;
	/**
	 * 本任务的目标
	 */
	private List<Target> targets;
	/**
	 * 对应的id
	 */
	private int id;
	public Targets() {}

	/**
	 * 创建一个Targets
	 * @param targetDefGetter 配置列表getter
	 * @param updateCallback 更新通知
	 * @param id 任务的id
	 * @return
	 */
	static Targets valueOf(TargetContainer container,
						   ITargetDefGetter targetDefGetter,
						   BiConsumer<Targets, Target> updateCallback,
						   int id) {
		Targets targets0 = new Targets();
		targets0.targets = Lists.newArrayListWithCapacity(targetDefGetter.getTargetList().size());
		targetDefGetter.getTargetList().forEach((index, def) -> targets0.targets.add(Target.valueOf(targetDefGetter, targets0, index)));
		targets0.updateCallback = updateCallback;
		targets0.container = container;
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
	void updateCallback(Target target){
		updateCallback.accept(this, target);
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

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
