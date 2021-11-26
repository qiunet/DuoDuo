package org.qiunet.function.condition;

import org.qiunet.utils.data.IKeyValueData;

import java.util.HashMap;
import java.util.Map;

/***
 * 条件配置
 * 一般是json格式.
 * [{"type": "PLAYER_MIN_LEVEL", "value": "30"}, {"type": "PLAYER_MAX_LEVEL", "value": "180"}]
 * 表示 最小30级, 最大180级.
 * @author qiunet
 * 2020-12-30 20:56
 */
public class ConditionConfig
		extends HashMap<Object, String> implements IKeyValueData<Object, String> {

	public String getType() {
		return getString("type");
	}

	@Override
	public Map<Object, String> returnMap() {
		return this;
	}
}
