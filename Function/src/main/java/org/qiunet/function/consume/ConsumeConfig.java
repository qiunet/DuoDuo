package org.qiunet.function.consume;

import org.qiunet.function.base.IResourceSubType;

import java.util.function.Function;

/***
 * 消耗配置的json类
 *
 * @author qiunet
 * 2020-12-28 11:50
 */
public class ConsumeConfig {
	/**
	 *  资源id
	 */
	private int cfgId;
	/**
	 * 数量
	 */
	private long count;
	/**
	 * 禁止替换
	 */
	private boolean banReplace;

	/**
	 * 转 Consume
	 * @param subTypeGetter cfgId -> subType
	 * @return Consume
	 */
	public BaseConsume convertToConsume(Function<Integer, IResourceSubType> subTypeGetter) {
		return subTypeGetter.apply(cfgId).createConsume(this);
	}

	public int getCfgId() {
		return cfgId;
	}

	public long getCount() {
		return count;
	}

	public boolean isBanReplace() {
		return banReplace;
	}
}
