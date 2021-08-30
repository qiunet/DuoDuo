package org.qiunet.utils.args;

/***
 * Argument 的key
 *
 * @author qiunet
 * 2020-10-16 16:38
 */
public final class ArgumentKey<T> {

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
	 * compare and set value
	 * @param container 容器对象
	 * @param oldVal 旧值
	 * @param newVal 新值
	 * @param <Container> 容器类
	 * @return cas的结果 true 为成功.
	 */
	public <Container extends IArgsContainer> boolean cas(Container container, T oldVal, T newVal) {
		return container.compareAndSet(this, oldVal, newVal);
	}
}
