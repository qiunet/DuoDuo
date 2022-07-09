package org.qiunet.utils.args;

import java.util.function.Supplier;

/***
 * Argument 的key
 *
 * @author qiunet
 * 2020-10-16 16:38
 */
public final class ArgumentKey<T> {

	Supplier<T> defaultValGetter;

	public ArgumentKey() {}

	public ArgumentKey(T defaultVal) {
		this(() -> defaultVal);
	}

	public ArgumentKey(Supplier<T> defaultValGetter) {
		this.defaultValGetter = defaultValGetter;
	}

	Argument<T> newAttribute() {
		return new Argument<>(this);
	}

	/**
	 * 从container get 到对应的值.
	 * @param container 容器对象
	 * @param <Container> 容器类
	 * @return 对应的值
	 */
	public <Container extends IArgsContainer> T get(Container container) {
		return container.getVal(this);
	}

	/**
	 * 设置指定的值到容器.
	 * @param container 容器对象
	 * @param newVal 值
	 * @param <Container> 容器类
	 * @return 返回旧值
	 */
	public <Container extends IArgsContainer> T set(Container container, T newVal) {
		return container.setVal(this, newVal);
	}

	/**
	 * 容器中指定的key是否有值.
	 * @param container 容器对象
	 * @param <Container> 容器类
	 * @return true 空
	 */
	public <Container extends IArgsContainer> boolean isNull(Container container){
		return container.isNull(this);
	}
	/**
	 * 容器中指定的key是否有值.
	 * @param container 容器对象
	 * @param <Container> 容器类
	 * @return true 非空
	 */
	public <Container extends IArgsContainer> boolean notNull(Container container){
		return ! container.isNull(this);
	}


	/**
	 * compare and set value
	 * @param container 容器对象
	 * @param oldVal 旧值
	 * @param newVal 新值
	 * @param <Container> 容器类
	 * @return cas的结果 true 为成功.
	 */
	public <Container extends IArgsContainer> boolean compareAndSet(Container container, T oldVal, T newVal) {
		return container.compareAndSet(this, oldVal, newVal);
	}

	/**
	 * 如果没有数据, 就使用指定的Supplier 初始化数据
	 * @param container 容器对象
	 * @param newVal 构造初始值的 Supplier
	 * @param <Container> 容器类型
	 * @return 现有的. 或者新的值
	 */
	public <Container extends IArgsContainer> T computeIfAbsent(Container container, Supplier<T> newVal) {
		return container.computeIfAbsent(this, newVal);
	}

	/**
	 * 删除 key
	 * @param container 容器对象
	 * @param <Container> 容器类型
	 */
	public <Container extends IArgsContainer> void remove(Container container) {
		container.removeArg(this);
	}

	/**
	 * 清除key
	 * @param container 容器对象
	 * @param <Container> 容器类型
	 */
	public <Container extends IArgsContainer> void clear(Container container) {
		container.clear(this);
	}
}
