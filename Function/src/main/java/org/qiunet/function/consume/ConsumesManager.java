package org.qiunet.function.consume;

import com.google.common.collect.Lists;
import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.function.base.basic.IBasicFunction;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.scanner.anno.AutoWired;

import java.util.Collections;
import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-12-28 15:17
 */
public enum ConsumesManager {
	instance;

	@AutoWired
	private static IBasicFunction resourceManager;
	/**
	 * 空的consumes
	 */
	public static final Consumes EMPTY_CONSUMES = new UnmodifiableConsumes(Collections.emptyList());
	/**
	 * 创建不可修改的 Consumes
	 * @param configList 配置列表
	 * @return Consumes
	 */
	public Consumes createConsumes(List<ConsumeConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return EMPTY_CONSUMES;
		}


		UnmodifiableConsumes unmodifiableConsumes = new UnmodifiableConsumes(configList);
		consumeList.add(unmodifiableConsumes);
		return unmodifiableConsumes;
	}

	private static final List<Consumes> consumeList = Lists.newLinkedList();
	/**
	 * 清理数据
	 * @param data
	 */
	@EventListener
	public void cfgLoadOver(CfgLoadCompleteEventData data) {
		consumeList.forEach(rewards -> ((IAfterLoad)rewards).afterLoad());
		consumeList.clear();
	}
}
