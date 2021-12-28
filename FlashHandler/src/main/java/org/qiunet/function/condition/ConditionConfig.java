package org.qiunet.function.condition;

import org.qiunet.utils.data.IKeyValueData;

import java.util.HashMap;
import java.util.Map;

/***
 * 条件配置
 * 一般是json格式. type的值不限制大小写.
 * [{"type": "PLAYER_MIN_LEVEL", "value": "30"}, {"type": "PLAYER_MAX_LEVEL", "value": "180"}]
 * 表示 最小30级, 最大180级.
 * 上述条件也可以定义为:
 * [{"type": "PLAYER_LEVEL", "minLevel": "30", "maxLevel": 180}]
 *
 * @author qiunet
 * 2020-12-30 20:56
 */
public class ConditionConfig
		extends HashMap<String, Object> implements IKeyValueData<String, Object> {

	public ConditionConfig(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	public ConditionConfig() {
		super(8);
	}

	public String getType() {
		return getString("type");
	}

	public void setType(Object type) {
		this.put("type", type);
	}

	@Override
	public Map<String, Object> returnMap() {
		return this;
	}
}
