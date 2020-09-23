package org.qiunet.flash.handler.context.request.data.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.util.Map;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:50
 */
class PbResponseDataMapping implements IApplicationContextAware {

	private static final Map<Class<? extends IpbResponseData>, PbResponse> mapping = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		for (Class<? extends IpbRequestData> clazz : context.getSubTypesOf(IpbRequestData.class)) {
			if (! clazz.isAnnotationPresent(ProtobufClass.class)) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify ProtobufClass annotation!");
			}
		}

		Set<Integer> protocolIds = Sets.newHashSet();
		for (Class<? extends IpbResponseData> clazz : context.getSubTypesOf(IpbResponseData.class)) {
			if (! clazz.isAnnotationPresent(ProtobufClass.class)) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify ProtobufClass annotation!");
			}

			if (! clazz.isAnnotationPresent(PbResponse.class)) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify PbResponse annotation!");
			}

			PbResponse annotation = clazz.getAnnotation(PbResponse.class);
			if (protocolIds.contains(annotation.ID())) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] specify protocol id ["+annotation.ID()+"] is repeated!");
			}

			protocolIds.add(annotation.ID());
			mapping.put(clazz, annotation);
		}

	}

	static int protocolId(Class<? extends IpbResponseData> clazz) {
		return mapping.get(clazz).ID();
	}

	static boolean skipDebugOut(Class<? extends IpbResponseData> clazz) {
		return mapping.get(clazz).skipDebugOut();
	}
}
