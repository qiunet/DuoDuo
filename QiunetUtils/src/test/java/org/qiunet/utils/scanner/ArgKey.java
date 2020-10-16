package org.qiunet.utils.scanner;

import org.qiunet.utils.args.IArgKey;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 12:08
 */
public class ArgKey<T> implements IArgKey<T> {

	public static ArgKey<Integer> Test = new ArgKey<>();

	private ArgKey() {}
}
