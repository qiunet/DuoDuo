package org.qiunet.utils.async;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/***
 * 并行处理事物
 *
 * @author qiunet
 * 2022/1/5 22:44
 */
public class ParallelProcess<E> {
	/**
	 * 需要处理的对象列表
	 */
	private final Collection<E> collection;

	private ParallelProcess(Collection<E> collection) {
		this.collection = collection;
	}

	public static <E> ParallelProcess<E> newProcess(Collection<E> collection) {
		return new ParallelProcess<>(collection);
	}

	/**
	 * 并发循环处理list中对象
	 * @param consumer
	 * @return
	 */
	public ParallelProcess<E> handle(Consumer<E> consumer) {
		return this.handle(null, consumer);
	}
	/**
	 * 并发循环处理list中对象
	 * @param consumer
	 * @return
	 */
	public ParallelProcess<E> handle(Predicate<E> filter, Consumer<E> consumer) {
		collection.parallelStream().filter(e -> filter == null || !filter.test(e)).forEach(consumer);
		return this;
	}
}
