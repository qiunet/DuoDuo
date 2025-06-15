package org.qiunet.function.item_change.consume;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.utils.thread.IThreadSafe;

/**
 * 普通物品的资源id
 * @author qiunet
 * 2023/12/22 11:36
 */
public abstract class BaseCfgConsume<Obj extends IThreadSafe & IPlayer> extends BaseConsume<Obj> {

	public BaseCfgConsume(Integer cfgId, long count) {
		super(cfgId, count);
	}

	public BaseCfgConsume(ItemChangeConfig config) {
		this(config.getCfgId(), config.getCount());
	}
}
