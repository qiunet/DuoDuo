package org.qiunet.function.item_change.consume;

import org.qiunet.function.item_change.ItemChangeConfig;

import java.util.Collections;
import java.util.List;

/***
 * 消费对象管理
 *
 * @author qiunet
 * 2020-12-28 15:17
 */
public enum ConsumesManager {
	instance;

	/**
	 * 空的consumes
	 */
	public static final Consumes EMPTY_CONSUMES = new UnmodifiableConsumes(Collections.emptyList());
	/**
	 * 创建不可修改的 Consumes
	 * @param configList 配置列表
	 * @return Consumes
	 */
	public Consumes createConsumes(List<ItemChangeConfig> configList) {
		if (configList == null || configList.isEmpty()) {
			return EMPTY_CONSUMES;
		}


		return new UnmodifiableConsumes(configList);
	}
}
