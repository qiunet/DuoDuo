package org.qiunet.utils.common.collector;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * Simple implementation class for {@code Collector}.
 *
 * @param <T> the type of elements to be collected
 * @param <R> the type of the result
 */
class CollectorImpl<T, A, R> implements Collector<T, A, R> {
	private final Supplier<A> supplier;
	private final BiConsumer<A, T> accumulator;
	private final BinaryOperator<A> combiner;
	private final Function<A, R> finisher;

	CollectorImpl(Supplier<A> supplier,
				  BiConsumer<A, T> accumulator,
				  BinaryOperator<A> combiner,
				  Function<A,R> finisher) {
		this.supplier = supplier;
		this.accumulator = accumulator;
		this.combiner = combiner;
		this.finisher = finisher;
	}

	CollectorImpl(Supplier<A> supplier,
				  BiConsumer<A, T> accumulator,
				  BinaryOperator<A> combiner) {
		this(supplier, accumulator, combiner, castingIdentity());
	}

	private static <I, R> Function<I, R> castingIdentity() {
		return i -> (R) i;
	}

	@Override
	public BiConsumer<A, T> accumulator() {
		return accumulator;
	}

	@Override
	public Supplier<A> supplier() {
		return supplier;
	}

	@Override
	public BinaryOperator<A> combiner() {
		return combiner;
	}

	@Override
	public Function<A, R> finisher() {
		return finisher;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}
}
