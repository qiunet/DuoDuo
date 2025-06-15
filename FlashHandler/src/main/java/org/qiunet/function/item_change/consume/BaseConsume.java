package org.qiunet.function.item_change.consume;

import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.function.item_change.ItemChangeElement;
import org.qiunet.utils.thread.IThreadSafe;

/**
 * 消耗父类
 * @param <Obj>
 */
public abstract class BaseConsume<Obj extends IThreadSafe & IPlayer> extends ItemChangeElement<BaseConsume<Obj>, ConsumeContext<Obj>> {

	public BaseConsume(int id, long count) {
		super(id, count);
	}
}
