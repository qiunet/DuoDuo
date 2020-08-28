package org.qiunet.utils.args;

/***
 *
 * @author qiunet
 * 2020-08-26 08:08
 **/
public class ArgsContainer0 implements IArgsContainer {
	private ArgsContainer container = new ArgsContainer(ArgKey.class);


	@Override
	public <T> Argument<T> getArgument(IArgKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}
}
