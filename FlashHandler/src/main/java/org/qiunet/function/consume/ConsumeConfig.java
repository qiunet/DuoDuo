package org.qiunet.function.consume;

import org.qiunet.function.base.IResourceType;
import org.qiunet.utils.data.IKeyValueData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/***
 * 消耗配置的json类
 *
 * @author qiunet
 * 2020-12-28 11:50
 */
public class ConsumeConfig  extends HashMap<Object, String> implements IKeyValueData<Object, String> {

	public ConsumeConfig() {}

	public ConsumeConfig(String cfgId, long value) {
		this(cfgId, value, false);
	}

	public ConsumeConfig(String cfgId, long value, boolean banReplace) {
		this.put("id", cfgId);
		this.put("value", String.valueOf(value));
		this.put("banReplace", String.valueOf(banReplace));
	}

	/**
	 * 转 Consume
	 * @param typeGetter cfgId → type
	 * @return Consume
	 */
	public BaseConsume convertToConsume(Function<String, IResourceType> typeGetter) {
		return typeGetter.apply(getCfgId()).createConsume(this);
	}

	/**
	 * 资源ID
	 * @return
	 */
	public String getCfgId() {
		return getString("id");
	}

	/**
	 * 数量
	 * @return
	 */
	public long getValue() {
		return getLong("value");
	}

	/**
	 * 是否禁止替换
	 * @return
	 */
	public boolean isBanReplace() {
		return getBoolean("banReplace");
	}

	@Override
	public Map<Object, String> returnMap() {
		return this;
	}
}
