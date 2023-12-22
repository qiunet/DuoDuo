package org.qiunet.function.consume;

import org.qiunet.utils.thread.IThreadSafe;

/**
 * 普通物品的资源id
 * @author qiunet
 * 2023/12/22 11:36
 */
public abstract class BaseCfgConsume<Obj extends IThreadSafe> extends BaseConsume<Obj> {

	public BaseCfgConsume(Integer cfgId, long count) {
		super(cfgId, count);
	}

	public BaseCfgConsume(ConsumeConfig consumeConfig) {
		this(consumeConfig.getCfgId(), consumeConfig.getCount());
	}

	@Override
	public Integer getId() {
		return (Integer) super.getId();
	}
}
