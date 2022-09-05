package org.qiunet.function.consume;

import com.alibaba.fastjson.TypeReference;
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
	private static final TypeReference<List<ConsumeConfig>> TYPE = new TypeReference<List<ConsumeConfig>>(){};

	@Override
	public Consumes fromString(Field field, String str) {
		List<ConsumeConfig> configList;
		if (StringUtil.isEmpty(str)) {
			configList = Collections.emptyList();
		}else {
			configList = JsonUtil.getGeneralObj(str, TYPE);
		}
		return ConsumesManager.instance.createConsumes(configList);
	}
}
