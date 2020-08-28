package org.qiunet.utils.args;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/***
 * 存储引用对象包装对象
 *
 * @author qiunet
 * 2020-08-25 21:35
 **/
public class Argument<T> {
	/**
	 * 保留一个map引用. 方便自删除
 	 */
	private Map<String, Argument> allArgsRef;
	/***
	 * 引用对象存储
	 */
	private AtomicReference<T> refData;

	private String name;
	Argument(String name) {
		this.name = name;
		this.refData = new AtomicReference<>();
	}

	/**
	 * set 一个存储值到对象
	 * @param newVal 需要存储的值
	 * @return 如果原本有值. 返回
	 */
	public T set(T newVal) {
		return this.refData.getAndSet(newVal);
	}

	/**
	 * cas操作存储值.
	 * @param expect 期望值
	 * @param newVal 新的值
	 * @return
	 */
	public boolean compareAndSet(T expect, T newVal){
		return this.refData.compareAndSet(expect, newVal);
	}
	/**
	 * 清除自身存储的数值
	 */
	public void clear() {
		this.refData.set(null);
	}
	/**
	 * 删除自身
	 */
	public void remove() {
		this.allArgsRef.remove(name);
	}
	/**
	 * 获得存储值
	 * @return
	 */
	public T get(){
		return refData.get();
	}

	/**
	 * 获得存储值. 如果没有. 使用默认值
	 * @param defaultVal 默认值
	 * @return
	 */
	public T get(T defaultVal) {
		if (isEmpty()) {
			return defaultVal;
		}
		return get();
	}

	/**
	 * 是否有存储值.
	 * @return 没有false
	 */
	public boolean isEmpty(){
		return refData.get() == null;
	}

	/**
	 * 获得名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Argument {" +
			"refData=" + refData.get() +
			", name='" + name + '\'' +
			'}';
	}

	void setAllArgsRef(Map<String, Argument> allArgsRef) {
		this.allArgsRef = allArgsRef;
	}
}
