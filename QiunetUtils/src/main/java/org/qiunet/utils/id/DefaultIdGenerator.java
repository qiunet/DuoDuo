package org.qiunet.utils.id;

import java.util.concurrent.atomic.AtomicLong;

/***
 *
 * @author qiunet
 * 2020-06-27 21:32
 **/
public class DefaultIdGenerator implements IdGenerator<Long> {

	private final AtomicLong generator;

	public DefaultIdGenerator() {
		this(0);
	}

	public DefaultIdGenerator(long initVal) {
		this.generator = new AtomicLong(initVal);
	}

	@Override
	public Long makeId() {
		return this.generator.incrementAndGet();
	}
}
