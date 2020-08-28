package org.qiunet.utils.args;

/***
 *
 * @author qiunet
 * 2020-08-26 08:09
 **/

public class ArgKey<T> extends AbstractArgKey<T> {
	public static final ArgKey<Boolean> TEST_KEY = new ArgKey<>("test_key");

	private ArgKey(String name) {
		super(name);
	}
}
