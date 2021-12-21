package org.qiunet.utils.collection.chain;

import com.google.common.collect.Lists;
import org.qiunet.utils.collection.enums.ForEachResult;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/***
 * 过滤的链表
 *
 * @author qiunet
 * 2021-01-06 20:31
 */
public class FilterChain<E> {
	/**
	 * 所有的过滤器
	 */
	private List<E> filters;

	/**
	 * 添加元素e
	 * @param e
	 */
	public void add(E e) {
		if (filters == null) {
			synchronized (this) {
				if (filters == null) {
					filters = Lists.newCopyOnWriteArrayList();
				}
			}
		}
		filters.add(e);
	}

	/**
	 * 删除指定元素e
	 * @param e
	 * @return
	 */
	public boolean remove(E e) {
		if (filters == null) {
			return true;
		}

		return filters.remove(e);
	}

	/**
	 * 是否包含 元素e
	 * @param e
	 * @return
	 */
	public boolean contains(E e) {
		return filters.contains(e);
	}

	/**
	 * 清理链表
	 */
	public void clean() {
		this.filters = null;
	}

	/**
	 * 所有的filter
	 * @return
	 */
	public List<E> all() {
		return filters == null ? Collections.emptyList() : filters;
	}

	/**
	 * 循环chain
	 * @param consumer
	 */
	public void forEach(Function<E, ForEachResult> consumer) {
		this.forEach(consumer, null);
	}

	/**
	 * 循环chain
	 * @param consumer
	 * @param predicate
	 */
	public void forEach(Function<E, ForEachResult> consumer, Predicate<E> predicate) {
		if (filters == null) {
			return;
		}

		for (E filter : this.filters) {
			if (predicate != null && !predicate.test(filter)) {
				continue;
			}

			ForEachResult result = consumer.apply(filter);
			if (result == ForEachResult.BREAK) {
				break;
			}
		}
	}
}

