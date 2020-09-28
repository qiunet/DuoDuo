package org.qiunet.utils.args;

/***
 *
 * @author qiunet
 * 2020-08-25 21:45
 **/
public abstract class AbstractArgKey<T> implements IArgKey<T> {
	private String name;

	protected AbstractArgKey(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}
}
