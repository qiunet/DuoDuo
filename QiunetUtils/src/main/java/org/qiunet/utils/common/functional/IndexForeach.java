package org.qiunet.utils.common.functional;

import org.qiunet.utils.collection.enums.ForEachResult;

/***
 *
 * @author qiunet
 * 2022/8/2 17:43
 */
@FunctionalInterface
public interface IndexForeach<E> {
	/**
	 * 消费 然后返回
	 * @param index
	 * @param e
	 * @return
	 */
	ForEachResult consume(int index, E e);
}
