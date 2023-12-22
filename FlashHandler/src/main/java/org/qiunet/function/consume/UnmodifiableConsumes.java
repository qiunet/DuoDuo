package org.qiunet.function.consume;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
public final class UnmodifiableConsumes<Obj extends IThreadSafe> extends Consumes<Obj> {
	private List<ConsumeConfig> consumeConfigs;
	/**
	 * 创建不可变更修改的消耗
	 * @param consumeConfigs 列表
	 */
	public UnmodifiableConsumes(List<ConsumeConfig> consumeConfigs) {
		super(Collections.emptyList());
		this.consumeConfigs = consumeConfigs;
	}

	@Override
	public void loadData() {
		if (consumeConfigs == null) {
			return;
		}
		List<BaseCfgConsume<Obj>> baseConsumeList = Lists.newArrayListWithCapacity(consumeConfigs.size());
		for (ConsumeConfig config : consumeConfigs) {
			BaseCfgConsume baseConsume = config.convertToConsume();
			if (baseConsume == null) {
				throw new CustomException("ConsumeConfig {} convert result is null", JsonUtil.toJsonString(config));
			}
			baseConsumeList.add(baseConsume);
		}
		super.consumeList = ImmutableList.copyOf(baseConsumeList);
		this.consumeConfigs = null;
	}
}
