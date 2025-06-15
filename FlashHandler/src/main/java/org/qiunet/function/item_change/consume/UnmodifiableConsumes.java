package org.qiunet.function.item_change.consume;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.Collections;
import java.util.List;

/***
 * 不可变更的消耗
 *
 * @author qiunet
 * 2020-12-28 11:59
 */
public final class UnmodifiableConsumes<Obj extends IThreadSafe& IPlayer> extends Consumes<Obj> {
	private List<ItemChangeConfig> consumeConfigs;
	/**
	 * 创建不可变更修改的消耗
	 * @param consumeConfigs 列表
	 */
	public UnmodifiableConsumes(List<ItemChangeConfig> consumeConfigs) {
		super(Collections.emptyList());
		this.consumeConfigs = consumeConfigs;
	}

	@Override
	public void loadData() {
		if (consumeConfigs == null) {
			return;
		}
		List<BaseConsume<Obj>> baseConsumeList = Lists.newArrayListWithCapacity(consumeConfigs.size());
		for (ItemChangeConfig config : consumeConfigs) {
			BaseCfgConsume<Obj> baseConsume = config.convertToConsumeItem();
			if (baseConsume == null) {
				throw new CustomException("ConsumeConfig {} convert result is null", JsonUtil.toJsonString(config));
			}
			baseConsumeList.add(baseConsume);
		}
		super.elements = baseConsumeList;
		this.consumeConfigs = null;
		this.setUnmodifiable();
	}
}
