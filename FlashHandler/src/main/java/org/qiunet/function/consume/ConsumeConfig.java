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
		this.put("id", String.valueOf(cfgId));
		this.put("count", String.valueOf(value));
	}

	/**
	 * 转 Consume
	 * @return Consume
	 */
	public BaseCfgConsume convertToConsume() {
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
	public long getCount() {
		return getLong("count");
	}

	@Override
	public Map<Object, String> returnMap() {
		return this;
	}
}
