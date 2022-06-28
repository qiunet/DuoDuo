package org.qiunet.utils.id;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2020-06-27 21:32
 **/
public class IntIdGenerator {

	private final AtomicInteger generator;

	public IntIdGenerator() {
		this(0);
	}

	public IntIdGenerator(int initVal) {
		this.generator = new AtomicInteger(initVal);
	}

	public int makeId() {
		return this.generator.incrementAndGet();
	}
}
