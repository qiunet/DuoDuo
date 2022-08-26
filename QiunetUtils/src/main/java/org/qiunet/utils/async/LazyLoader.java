package org.qiunet.utils.async;

import com.google.common.collect.Lists;

import java.util.List;
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
	/**
	 * 监听
	 */
	private List<Listener<T>> listeners;

	private final Supplier<T> supplier;

	public LazyLoader(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	/**
	 * 获取值. 如果为空. 会使用构造函数两面的 Supplier load 一个.
	 * 值不为null, 下次直接读取报错的值.
	 * @return
	 */
	public T get(){
		T ret = objRef.get();
		if (ret == null) {
			ret = supplier.get();
			if (ret != null && objRef.compareAndSet(null, ret) && listeners != null) {
				synchronized (this) {
					T finalRet = ret;
					listeners.forEach(l -> l.run(finalRet));
				}
			}
		}
		return ret;
	}

	/**
	 * 添加 loader 有值时候的监听
	 * 可以重复添加
	 * @param listener 监听
	 */
	public synchronized void addCompleteListener(Listener<T> listener) {
		if (this.listeners == null) {
			this.listeners = Lists.newArrayListWithCapacity(3);
		}
		this.listeners.add(listener);
	}

	/**
	 * 重置loader
	 */
	public synchronized void reset(boolean cleanListener){
		if (cleanListener) {
			this.listeners = null;
		}
		this.objRef.set(null);
	}

	/**
	 * 是否为null
	 * @return
	 */
	public boolean isNull() {
		return this.objRef.get() == null;
	}

	/**
	 * 是否不为null
	 * @return
	 */
	public boolean isNotNull() {
		return ! this.isNull();
	}

	@FunctionalInterface
	public interface Listener<T> {

		void run(T t);
	}
}
