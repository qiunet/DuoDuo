package org.qiunet.utils.collection.safe;

/***
 *
 *
 * @author qiunet
 * 2020-03-11 09:38
 ***/
public interface ISafeCollection {
	/**
	 * 将集合转为安全集合.
	 * 不可写.
	 */
	void convertToUnmodifiable();
}
