package org.qiunet.utils.args;

/***
 * 持有{@link ArgsContainer}的对象. 可以实现该接口.
 * 已经有大部分默认方法实现了
 *
 * @author qiunet
 * 2020-08-26 07:59
 **/
public interface IArgsContainer {
	/**
	 * 获取 {@link Argument} 没有会默认创建
	 * @param key
	 * @param <T>
	 * @return
	 */
	default <T> Argument<T> getArgument(ArgumentKey<T> key){
		return getArgument(key, true);
	}

	/**
	 * 获取 {@link Argument} 根据 @param computeIfAbsent 判断是否创建.
	 * @param key
	 * @param computeIfAbsent
	 * @param <T>
	 * @return
	 */
	<T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent);
	/**
	 * 删除自身
	 */
	default void removeArg(ArgumentKey key) {
		Argument argument = getArgument(key, false);
		if (argument != null) argument.remove();
	}
	/**
	 * 清除自身存储的数值
	 */
	default void clear(ArgumentKey key) {
		Argument argument = getArgument(key, false);
		if (argument != null) argument.clear();
	}

	/***
	 * 得到一个对象的值.
	 * @param key
	 * @param <T>
	 * @return
	 */
	default <T> T getVal(ArgumentKey<T> key) {
		return getArgument(key).get();
	}

	/**
	 * set 一个新的值. 并返回旧值
	 * @param key
	 * @param newVal
	 * @param <T>
	 * @return
	 */
	default <T> T setVal(ArgumentKey<T> key, T newVal) {
		return getArgument(key).set(newVal);
	}

	/**
	 * get 存储值. 没有返回默认的值. 避免null时候. 有空指针.
	 * @param key
	 * @param defaultVal
	 * @param <T>
	 * @return
	 */
	default <T> T getVal(ArgumentKey<T> key, T defaultVal) {
		return getArgument(key).get(defaultVal);
	}

	/**
	 * cas级别设置存储值
	 * @param key
	 * @param expect
	 * @param newVal
	 * @param <T>
	 * @return
	 */
	default <T> boolean compareAndSet(ArgumentKey<T> key, T expect, T newVal) {
		return getArgument(key).compareAndSet(expect, newVal);
	}

	/**
	 * 是否有存储值
	 * @return
	 */
	default boolean isNull(ArgumentKey key) {
		Argument argument = getArgument(key, false);
		return argument == null || argument.isNull();
	}
}
