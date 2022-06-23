package org.qiunet.function.consume;

import org.qiunet.function.base.basic.IBasicFunction;
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


		return new UnmodifiableConsumes(configList);
	}
}
