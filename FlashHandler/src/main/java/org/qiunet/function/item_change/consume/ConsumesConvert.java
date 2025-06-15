package org.qiunet.function.item_change.consume;

import com.alibaba.fastjson2.TypeReference;
import org.qiunet.function.item_change.ItemChangeConfig;
import org.qiunet.utils.convert.BaseObjConvert;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/***
 * Consumes 转换器
 *
 * @author qiunet
 * 2020-12-28 16:16
 */
public class ConsumesConvert extends BaseObjConvert<Consumes> {
	private static final TypeReference<List<ItemChangeConfig>> TYPE = new TypeReference<List<ItemChangeConfig>>(){};

	@Override
	public Consumes fromString(Field field, String str) {
		List<ItemChangeConfig> configList;
		if (StringUtil.isEmpty(str)) {
			configList = Collections.emptyList();
		}else {
			configList = JsonUtil.getGeneralObj(str, TYPE);
		}
		return ConsumesManager.instance.createConsumes(configList);
	}
}
