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
	private long value;
	/**
	 * 禁止替换
	 */
	private boolean banReplace;

	public ConsumeConfig() {}

	public ConsumeConfig(int cfgId, long value) {
		this(cfgId, value, false);
	}

	public ConsumeConfig(int cfgId, long value, boolean banReplace) {
		this.cfgId = cfgId;
		this.value = value;
		this.banReplace = banReplace;
	}

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

	public long getValue() {
		return value;
	}

	public boolean isBanReplace() {
		return banReplace;
	}
}
