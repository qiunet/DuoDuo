package org.qiunet.utils.scanner;

import org.qiunet.utils.args.AbstractArgKey;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 12:08
 */
public class ArgKey<T> extends AbstractArgKey<T> {

	public static ArgKey<Integer> Test = new ArgKey<>("test");

	protected ArgKey(String name) {
		super(name);
	}
}
