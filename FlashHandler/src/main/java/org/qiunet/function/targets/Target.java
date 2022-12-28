package org.qiunet.function.targets;

import com.alibaba.fastjson2.annotation.JSONField;
import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.PlayerActor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/***
 * 单个目标的进度管理
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public class Target {
	/**
	 * 更新回调
	 */
	@JSONField(serialize = false)
	transient Consumer<Target> updateCallback;
	/**
	 * 目标容器
	 */
	@JSONField(serialize = false)
	transient TargetContainer container;
	/**
	 * 用户数据. 可以序列化的对象
	 */
	private Map<String, String> userdata;

	@JSONField(serialize = false)
	transient ITargetDef targetDef;

	/**
	 * 任务目标的配置定义ID
	 */
	private int tid;
	/**
	 * 进度值
	 */
	private long value;

	static Target valueOf(PlayerActor player, Consumer<Target> updateCallback, ITargetDef targetDef) {
		Target target = new Target();
		target.container = TargetContainer.get(player);
		target.updateCallback = updateCallback;
		target.tid = targetDef.getId();
		target.targetDef = targetDef;
		return target;
	}
	/**
	 * 获得玩家对象
	 * @return actor
	 */
	public PlayerActor getPlayer() {
		return container.getPlayer();
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
		updateCallback.accept(this);
		this.tryFinish();
	}

	/**
	 * 设置进度 并且 尝试完成
	 * @param count 数量
	 */
	public synchronized void alterToCount(int count) {
		if (isFinished()) {
			return;
		}
		this.value = count;
		updateCallback.accept(this);
		this.tryFinish();
	}

	private void tryFinish() {
		if (isFinished()) {
			// 可能在forEach时候. unwatch了
			this.remove();
		}
	}

	/**
	 * 获得用户数据
	 * @param key
	 * @return
	 */
	public String getUserdata(String key) {
		if (userdata == null) {
			return null;
		}
		return userdata.get(key);
	}
	/**
	 * 清理某个key
	 * @param key
	 */
	public void removeUserdata(String key) {
		if (userdata != null) {
			userdata.remove(key);
		}
	}
	/**
	 * 增加用户数据
	 * @param key
	 * @param data
	 */
	public void addUserdata(String key, String data) {
		if (this.userdata == null) {
			this.userdata = new HashMap<>();
		}
		this.userdata.put(key, data);
	}
	/**
	 * 移除自己
	 */
	public void remove() {
		// 可能在forEach时候. unwatch了
		getPlayer().addMessage((p0) -> {
			container.unWatch(this);
		});
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
}
