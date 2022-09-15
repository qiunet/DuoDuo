package org.qiunet.function.targets;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.thread.ThreadPoolManager;

/***
 * 单个目标的进度管理
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public class Target {
	@JSONField(serialize = false)
	transient ITargetDef targetDef;

	@JSONField(serialize = false)
	transient Targets targets;
	/**
	 * 任务目标的配置定义ID
	 */
	private int tid;
	/**
	 * 进度值
	 */
	private long value;

	static Target valueOf(Targets targets, ITargetDef targetDef) {
		Target target = new Target();
		target.tid = targetDef.getId();
		target.targetDef = targetDef;
		target.targets = targets;
		return target;
	}
	/**
	 * 获得玩家对象
	 * @return actor
	 */
	public PlayerActor getPlayer() {
		return targets.getPlayer();
	}
	/**
	 * 进度 + 1
	 */
	public void addCount(){
		this.addCount(1);
	}

	/**
	 * 增加进度 并且 尝试完成
	 * @param count 数量
	 */
	public synchronized void addCount(long count){
		if (isFinished()) {
			return;
		}
		Preconditions.checkState(count > 0);
		this.value += count;
		targets.updateCallback(this);
		this.tryFinish();
	}

	/**
	 * 设置进度 并且 尝试完成
	 * @param count 数量
	 */
	public synchronized void alterToCount(int count) {
		// = 0 可能为gm重置任务
		Preconditions.checkState(count >= 0);
		if (isFinished()) {
			return;
		}
		this.value = count;
		targets.updateCallback(this);
		this.tryFinish();
	}

	private void tryFinish() {
		if (isFinished()) {
			// 可能在forEach时候. unwatch了
			getPlayer().addMessage((p0) -> {
				TargetContainer.get(p0).unWatch(this);
			});
		}
	}

	@JSONField(serialize = false)
	public boolean isFinished(){
		return value >= targetDef.getValue();
	}

	@JSONField(serialize = false)
	public ITargetDef getTargetDef() {
		return targetDef;
	}

	public int getTid() {
		return tid;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}
