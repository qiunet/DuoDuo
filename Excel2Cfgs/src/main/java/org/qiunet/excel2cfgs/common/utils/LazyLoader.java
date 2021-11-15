package org.qiunet.excel2cfgs.common.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/***
 * 延迟加载的对象.
 * 并非马上就有. get时候. 没有再加载.
 *
 * @author qiunet
 * 2020-09-23 22:47
 **/
public class LazyLoader<T> {

	private final AtomicReference<T> objRef = new AtomicReference<>();

	private final Supplier<T> supplier;

	public LazyLoader(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get(){
		T ret = objRef.get();
		if (ret == null) {
			objRef.compareAndSet(null, ret = supplier.get());
		}
		return ret;
	}

	/**
	 * 重置loader
	 */
	public void reset(){
		this.objRef.set(null);
	}
}
