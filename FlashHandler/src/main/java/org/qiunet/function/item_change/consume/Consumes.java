package org.qiunet.function.item_change.consume;

import com.google.common.collect.Lists;
import org.qiunet.cfg.base.ICfgDelayLoadData;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.AbstractItemChange;
import org.qiunet.function.item_change.ItemChangeVerifyParams;
import org.qiunet.utils.thread.IThreadSafe;

import java.util.List;

public class Consumes<Obj extends IThreadSafe & IPlayer> extends AbstractItemChange<Consumes<Obj>, Obj, ConsumeContext<Obj>, BaseConsume<Obj>> implements ICfgDelayLoadData {


	public Consumes() {
		this(Lists.newArrayListWithCapacity(3));
	}

	public Consumes(List<BaseConsume<Obj>> consumeList) {
		super(consumeList);
	}

	@Override
	protected Consumes<Obj> newInstance() {
		return new Consumes<>();
	}

	@Override
	protected ConsumeContext<Obj> newContext(ItemChangeVerifyParams<Obj> params) {
		return new ConsumeContext<>(params, this);
	}
}
