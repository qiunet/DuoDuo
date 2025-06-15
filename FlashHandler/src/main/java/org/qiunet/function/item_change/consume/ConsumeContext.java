package org.qiunet.function.item_change.consume;

import org.qiunet.function.item_change.AbstractItemChange;
import org.qiunet.function.item_change.ItemChangeContext;
import org.qiunet.function.item_change.ItemChangeVerifyParams;

/**
 *  消耗的上下文
 * @param <Obj>
 */
public class ConsumeContext<Obj> extends ItemChangeContext<Obj> {

	public ConsumeContext(ItemChangeVerifyParams<Obj> verifyParams, AbstractItemChange items) {
		super(verifyParams, items);
	}

	@Override
	protected void triggerEvent() {
		ConsumeEvent.valueOf(this).fireEventHandler();
	}
}
