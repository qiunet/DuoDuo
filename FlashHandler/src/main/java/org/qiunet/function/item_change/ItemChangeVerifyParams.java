package org.qiunet.function.item_change;

import org.qiunet.function.base.IReasonType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;

public class ItemChangeVerifyParams<Obj> implements IArgsContainer {

	private ArgsContainer container;

	private final IReasonType reasonType;

	private final Obj obj;

	public ItemChangeVerifyParams(Obj obj, IReasonType reasonType) {
		this.reasonType = reasonType;
		this.obj = obj;
	}

	public static <Obj> ItemChangeVerifyParams<Obj> valueOf(Obj obj, IReasonType reasonType) {
		return new ItemChangeVerifyParams<>(obj, reasonType);
	}

	private ArgsContainer getContainer() {
		if (container == null) {
			container = new ArgsContainer();
		}
		return container;
	}

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return getContainer().getArgument(key, computeIfAbsent);
	}

	public IReasonType getReasonType() {
		return reasonType;
	}

	public Obj getObj() {
		return obj;
	}
}

