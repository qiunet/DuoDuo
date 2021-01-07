package org.qiunet.utils.args;

/***
 * Argument 的key
 *
 * @author qiunet
 * 2020-10-16 16:38
 */
public final class ArgumentKey<T> {

	Argument<T> newAttribute() {
		return new Argument<>(this);
	}
}
