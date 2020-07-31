package org.qiunet.utils.id;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * @author qiunet
 * 2020-06-27 21:32
 **/
public class DefaultIdGenerator implements IdGenerator {

	private AtomicInteger generator;

	public DefaultIdGenerator() {
		this(0);
	}

	public DefaultIdGenerator(int initVal) {
		this.generator = new AtomicInteger(initVal);
	}

	@Override
	public int makeId() {
		return this.generator.incrementAndGet();
	}
}
