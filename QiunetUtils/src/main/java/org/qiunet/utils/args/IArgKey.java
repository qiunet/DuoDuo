package org.qiunet.utils.args;

/***
 * 属性的名称.
 *
 * @author qiunet
 * 2020-08-25 21:34
 **/
public interface IArgKey<T> {

	/**
	 * new 一个Attribute对象
	 * @return
	 */
	default Argument<T> newAttribute() {
		return new Argument<>(this);
	}
}
