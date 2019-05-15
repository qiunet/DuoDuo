package org.qiunet.utils.collection.safe;

import org.qiunet.utils.exceptions.SafeColletionsModifyException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/3/1 16:35.
 */
public class SafeHashMap<KEY, VAL> extends LinkedHashMap<KEY, VAL> {
	private Logger logger = LoggerType.DUODUO.getLogger();
	/**
	 * 一个只允许初始化一次的锁变量
	 */
	private boolean safeLock;
	/***
	 * 缺失打印.
	 */
	private boolean loggerAbsent;
	@Override
	public VAL put(KEY key, VAL value) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.put(key, value);
	}

	@Override
	public VAL get(Object key) {
		VAL val =  super.get(key);
		if (loggerAbsent && val == null) {
			logger.error("=================Key ["+key+"] is not in map.=============== ");
		}
		return val;
	}

	@Override
	public void putAll(Map<? extends KEY, ? extends VAL> m) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		super.putAll(m);
	}

	@Override
	public VAL remove(Object key) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.remove(key);
	}
	/**
	 * 把当前的list设置为锁定状态. 不允许修改里面的数据
	 */
	public void safeLock() {
		this.safeLock = true;
	}

	/**
	 * 如果缺失.则打印
	 */
	public void loggerIfAbsent() {
		this.loggerAbsent = true;
	}

	@Override
	public void clear() {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		super.clear();
	}
}
