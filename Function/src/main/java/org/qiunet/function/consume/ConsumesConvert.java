package org.qiunet.function.consume;

import com.alibaba.fastjson.TypeReference;
import org.qiunet.cfg.convert.BaseObjConvert;
import org.qiunet.utils.json.JsonUtil;

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
	protected Consumes fromString0(String str) {
		List<ConsumeConfig> configList = JsonUtil.getGeneralObjWithField(str, TYPE);
		return ConsumesManager.instance.createConsumes(configList);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Consumes.class;
	}
}
