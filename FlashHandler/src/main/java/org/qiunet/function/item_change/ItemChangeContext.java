package org.qiunet.function.item_change;

import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.function.base.IReasonType;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemChangeContext<Obj> implements IArgsContainer {
	protected StatusResult status = StatusResult.SUCCESS;

	protected final ItemChangeVerifyParams<Obj> verifyParams;

	protected final AbstractItemChange items;

	protected final List<IItemChange> changes;

	private boolean successCheck;

	private boolean acted;

	public ItemChangeContext(ItemChangeVerifyParams<Obj> verifyParams, AbstractItemChange items) {
		this.changes = new ArrayList<>(items.size());
		this.verifyParams = verifyParams;
		this.items = items;
	}

	public StatusResult getStatus() {
		return status;
	}

	public boolean isSuccess() {
		this.successCheck = true;
		return getStatus().isSuccess();
	}

	public boolean isFail(){
		return !isSuccess();
	}

	public void failThrowException() {
		if (isSuccess()) {
			return;
		}
		throw  StatusResultException.valueOf(getStatus());
	}

	public void failThrowExceptionOrAct() {
		this.failThrowException();
		this.act();
	}

	public void act() {
		if (this.acted) {
			throw new IllegalStateException("ItemChange has already been acted.");
		}

		if (!this.successCheck) {
			throw new IllegalStateException("ItemChangeContext must check isSuccess() before act().");
		}

		this.acted = true;
		if (isSuccess() && this.changes.isEmpty()) {
			return; // Nothing to do
		}

		this.items.act(this);
		this.triggerEvent();
	}

	protected abstract void triggerEvent();

	public ItemChangeVerifyParams<Obj> getVerifyParams() {
		return verifyParams;
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return getVerifyParams().getArgument(key, computeIfAbsent);
	}

	public IReasonType getReasonType() {
		return getVerifyParams().getReasonType();
	}

	public Obj getObj() {
		return getVerifyParams().getObj();
	}
}
