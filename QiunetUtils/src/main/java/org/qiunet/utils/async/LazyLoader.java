package org.qiunet.utils.async;

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

	private AtomicReference<T> obj = new AtomicReference<>();

	private Supplier<T> supplier;

	public LazyLoader(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get(){
		T ret = obj.get();
		if (ret == null) {
			obj.compareAndSet(null, ret = get0());
		}
		return ret;
	}

	private T get0() {
		return supplier.get();
	}
}
