package org.qiunet.flash.handler.context.request.check;

import com.google.common.collect.Lists;
import org.qiunet.flash.handler.context.request.check.cd.RequestCD;
import org.qiunet.flash.handler.context.request.check.cd.RequestCdCheck;
import org.qiunet.flash.handler.context.request.check.param.ParamCheckType;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.reflect.ReflectUtil;

import java.util.Collections;
import java.util.List;

/***
 *
 * @author qiunet
 * 2022/1/5 18:17
 */
public class RequestCheckList {
	/**
	 * 有的列表
	 */
	private final List<IRequestCheck> list;

	private RequestCheckList(List<IRequestCheck> list) {
		this.list = Collections.unmodifiableList(list);
	}

	/**
	 * 检查请求
	 * @param data
	 */
	public void check(ISession session, IChannelData data) {
		list.forEach(check -> check.check(session, data));
	}

	/**
	 * 解析  IChannelData class
	 * @param clazz
	 * @return
	 */
	public static RequestCheckList doParse(int protocolID, Class<? extends IChannelData> clazz) {
		List<IRequestCheck> list = Lists.newArrayListWithCapacity(4);
		if (clazz.isAnnotationPresent(RequestCD.class)) {
			list.add(new RequestCdCheck(protocolID, clazz.getAnnotation(RequestCD.class)));
		}

		ReflectUtil.doWithFields(clazz, field -> {
			for (ParamCheckType value : ParamCheckType.values) {
				if (value.match(field)) {
					list.add(value.build(field));
				}
			}
		});
		return list.isEmpty() ? null : new RequestCheckList(list);
	}
}
