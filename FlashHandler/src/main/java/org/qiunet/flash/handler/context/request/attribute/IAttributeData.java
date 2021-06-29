package org.qiunet.flash.handler.context.request.attribute;

/**
 * 可以附带存储  get  set 的对象接口
 * Created by qiunet.
 * 17/11/20
 */
public interface IAttributeData {
	/**
	 * 得到对象
	 * @param key
	 * @return
	 */
	Object getAttribute(String key);

	/**
	 * 获取或者得到默认值
	 * @param key
	 * @return
	 */
	default Object getOrDefault(String key, Object defaultVal) {
		Object attribute = getAttribute(key);
		if (attribute == null) {
			return defaultVal;
		}
		return attribute;
	}
	/**
	 * 得到对象
	 * @param key
	 * @return
	 */
	void setAttribute(String key, Object val);
}
