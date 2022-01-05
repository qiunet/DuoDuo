package org.qiunet.flash.handler.context.request.param.check;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.reflect.ReflectUtil;

import java.util.Collections;
import java.util.List;

/***
 *
 * @author qiunet
 * 2022/1/5 18:17
 */
public class ParamCheckList {
	/**
	 * 有的列表
	 */
	private final List<IParamCheck> list;

	private ParamCheckList(List<IParamCheck> list) {
		this.list = Collections.unmodifiableList(list);
	}

	/**
	 * 检查参数
	 * @param data
	 */
	public void check(IChannelData data) {
		list.forEach(check -> check.check(data));
	}

	/**
	 * 解析  IChannelData class
	 * @param clazz
	 * @return
	 */
	public static ParamCheckList doParse(Class<? extends IChannelData> clazz) {
		List<IParamCheck> list = Lists.newArrayListWithCapacity(4);
		ReflectUtil.doWithFields(clazz, field -> {
			for (ParamCheckType value : ParamCheckType.values) {
				if (value.match(field)) {
					list.add(value.build(field));
				}
			}
		});
		return list.isEmpty() ? null : new ParamCheckList(list);
	}
}
