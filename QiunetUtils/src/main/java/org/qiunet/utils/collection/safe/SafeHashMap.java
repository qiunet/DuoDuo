package org.qiunet.utils.collection.safe;

import org.qiunet.utils.exceptions.SafeColletionsModifyException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

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
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
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
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		super.putAll(m);
	}

	@Override
	public VAL remove(Object key) {
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		return super.remove(key);
	}

	@Override
	public VAL compute(KEY key, BiFunction<? super KEY, ? super VAL, ? extends VAL> remappingFunction) {
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		return super.compute(key, remappingFunction);
	}

	@Override
	public VAL computeIfAbsent(KEY key, Function<? super KEY, ? extends VAL> mappingFunction) {
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		return super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public VAL computeIfPresent(KEY key, BiFunction<? super KEY, ? super VAL, ? extends VAL> remappingFunction) {
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		return super.computeIfPresent(key, remappingFunction);
	}

	@Override
	public VAL merge(KEY key, VAL value, BiFunction<? super VAL, ? super VAL, ? extends VAL> remappingFunction) {
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		return super.merge(key, value, remappingFunction);
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
		if (safeLock) {
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		}
		super.clear();
	}
}
