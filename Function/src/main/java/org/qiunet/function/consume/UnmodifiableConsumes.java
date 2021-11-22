package org.qiunet.function.consume;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.flash.handler.common.IThreadSafe;

import java.util.List;

/***
 * 不可变更的消耗
 *
 * @author qiunet
 * 2020-12-28 11:59
 */
public final class UnmodifiableConsumes<Obj extends IThreadSafe> extends Consumes<Obj> implements IAfterLoad {
	private List<ConsumeConfig> consumeConfigs;
	/**
	 * 创建不可变更修改的消耗
	 * @param consumeConfigs 列表
	 */
	public UnmodifiableConsumes(List<ConsumeConfig> consumeConfigs) {
		super();
		this.consumeConfigs = consumeConfigs;
	}

	@Override
	public void afterLoad() {
		if (this.consumeConfigs.isEmpty()) {
			return;
		}

		List<BaseConsume<Obj>> baseConsumeList = Lists.newArrayListWithCapacity(consumeConfigs.size());
		for (ConsumeConfig config : consumeConfigs) {
			baseConsumeList.add(config.convertToConsume(id -> basicFunction.getResType(id)));
		}
		super.consumeList = ImmutableList.copyOf(baseConsumeList);
		consumeConfigs = null;
	}
}
