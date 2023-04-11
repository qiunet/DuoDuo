package org.qiunet.function.consume;

import org.qiunet.cfg.manager.base.LoadSandbox;
import org.qiunet.function.base.IResourceCfg;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.exceptions.CustomException;

import java.util.HashMap;
import java.util.Map;

/***
 * 消耗配置的json类
 *
 * @author qiunet
 * 2020-12-28 11:50
 */
public class ConsumeConfig  extends HashMap<Object, String> implements IKeyValueData<Object, String> {

	public ConsumeConfig() {}

	public ConsumeConfig(int cfgId, long value) {
		this(cfgId, value, false);
	}

	public ConsumeConfig(int cfgId, long value, boolean banReplace) {
		this.put("id", String.valueOf(cfgId));
		this.put("value", String.valueOf(value));
		this.put("banReplace", String.valueOf(banReplace));
	}

	/**
	 * 转 Consume
	 * @return Consume
	 */
	public BaseConsume convertToConsume() {
		IResourceCfg res = LoadSandbox.instance.getResById(getCfgId());
		if (res == null) {
			throw new CustomException("Res id {} null point exception:", getCfgId());
		}
		return res.type().createConsume(this);
	}

	/**
	 * 资源ID
	 * @return
	 */
	public int getCfgId() {
		return getInt("id");
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
