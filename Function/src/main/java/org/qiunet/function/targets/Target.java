package org.qiunet.function.targets;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Preconditions;

/***
 * 单个目标的进度管理
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public class Target {
	@JSONField(serialize = false)
	private transient ITargetDef targetDef;

	@JSONField(serialize = false)
	private transient Targets targets;
	/**
	 * 目标的配置定义索引
	 */
	private int index;
	/**
	 * 进度值
	 */
	private int value;

	static Target valueOf(ITargetDefGetter targetDefGetter, Targets targets, int index) {
		Target target = new Target();
		target.targetDef = targetDefGetter.getTargetDef(index);
		target.targets = targets;
		return target;
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
	public synchronized void addCount(int count){
		Preconditions.checkState(count > 0);
		this.value += count;
		tryComplete();
	}

	/**
	 * 设置进度 并且 尝试完成
	 * @param count 数量
	 */
	@JSONField(deserialize = false)
	public synchronized void setCount(int count) {
		Preconditions.checkState(count > 0);
		this.value += count;
		tryComplete();
	}

	/**
	 * 尝试完成该目标
	 */
	void tryComplete() {
		if (isFinished()) {
			targets.getContainer().unWatch(this);
			if (targets.isFinished()) {
				targets.finishCallback();
			}
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
