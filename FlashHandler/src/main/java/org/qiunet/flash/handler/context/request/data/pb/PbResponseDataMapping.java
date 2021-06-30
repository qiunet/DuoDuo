package org.qiunet.flash.handler.context.request.data.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:50
 */
public class PbResponseDataMapping implements IApplicationContextAware {

	private static final Map<Class<? extends IpbResponseData>, Integer> mapping = Maps.newHashMap();

	private PbResponseDataMapping(){}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Integer> protocolIds = Sets.newHashSet();
		for (Class<? extends IpbChannelData> clazz : context.getSubTypesOf(IpbChannelData.class)) {
			if (Modifier.isAbstract(clazz.getModifiers())
			 || Modifier.isInterface(clazz.getModifiers())
			) {
				continue;
			}

			ProtobufClass protobufClass = clazz.getAnnotation(ProtobufClass.class);
			if (protobufClass == null) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify ProtobufClass annotation!");
			}

			if (StringUtil.isEmpty(protobufClass.description())) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify ProtobufClass description!");
			}

			if (! IpbResponseData.class.isAssignableFrom(clazz)) {
				continue;
			}

			if (! clazz.isAnnotationPresent(PbChannelDataID.class)) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify PbResponse annotation!");
			}

			PbChannelDataID pbChannelDataID = clazz.getAnnotation(PbChannelDataID.class);
			if (protocolIds.contains(pbChannelDataID.value())) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] specify protocol value ["+ pbChannelDataID.value()+"] is repeated!");
			}

			protocolIds.add(pbChannelDataID.value());
			mapping.put((Class<? extends IpbResponseData>) clazz, pbChannelDataID.value());
		}
	}

	public static int protocolId(Class<? extends IpbResponseData> clazz) {
		return mapping.get(clazz);
	}
}
