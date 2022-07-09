package org.qiunet.utils.args;

/***
 *
 * 存储所有Attribute对象的地方.
 *
 * @author qiunet
 * 2020-08-25 21:50
 **/
public class ArgsContainer implements IArgsContainer {
	/**
	 * 能根绝container 保存不同的Argument.
	 */
	private final ArgsDataMap argumentMap = new ArgsDataMap();


	/**
	 * 得到一个Attribute对象
	 * @param key
	 * @param <T>
	 * @return 不会返回null
	 */

	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		if (! computeIfAbsent) {
			return argumentMap.get(key);
		}
		return argumentMap.computeIfAbsent(key);
	}


	public void clear() {
		this.argumentMap.clear();
	}
}
