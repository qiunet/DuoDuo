package org.qiunet.utils.args;

/***
 *
 * 存储所有Attribute对象的地方.
 *
 * @author qiunet
 * 2020-08-25 21:50
 **/
public class ArgsContainer implements IArgsContainer {
	private Class<? extends IArgKey> keyClass;
	private ArgsDataMap attributes = new ArgsDataMap();

	public ArgsContainer() {
		this.keyClass = null;
	}

	public ArgsContainer(Class<? extends IArgKey> keyClass) {
		this.keyClass = keyClass;
	}

	/**
	 * 得到一个Attribute对象
	 * @param key
	 * @param <T>
	 * @return 不会返回null
	 */

	@Override
	public <T> Argument<T> getArgument(IArgKey<T> key, boolean computeIfAbsent) {
		if (keyClass != null && key.getClass() != keyClass) {
			throw new IllegalArgumentException("It,s ArgsDataMap for class ["+keyClass.getName()+"]");
		}
		if (! computeIfAbsent) {
			return attributes.get(key);
		}
		return attributes.computeIfAbsent(key);
	}

}
