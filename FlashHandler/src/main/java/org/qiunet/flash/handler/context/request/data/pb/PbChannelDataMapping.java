package org.qiunet.flash.handler.context.request.data.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/***
 * Protobuf data Id 和 类的映射关系.
 *
 * @author qiunet
 * 2020-09-22 12:50
 */
public class PbChannelDataMapping implements IApplicationContextAware {

	private static final Map<Class<? extends IpbChannelData>, Integer> mapping = Maps.newHashMap();

	private PbChannelDataMapping(){}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Integer> protocolIds = Sets.newHashSet();
		ByteBuddyAgent.install();
		for (Class<? extends IpbChannelData> clazz : context.getSubTypesOf(IpbChannelData.class)) {
			if (Modifier.isAbstract(clazz.getModifiers())
			 || Modifier.isInterface(clazz.getModifiers())
			) {
				continue;
			}

			if (! clazz.isAnnotationPresent(PbChannelDataID.class)) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify PbResponse annotation!");
			}

			PbChannelDataID pbChannelDataID = clazz.getAnnotation(PbChannelDataID.class);
			if (protocolIds.contains(pbChannelDataID.ID())) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] specify protocol value ["+ pbChannelDataID.ID()+"] is repeated!");
			}

			new ByteBuddy().redefine(clazz).annotateType(
					AnnotationDescription.Builder.ofType(ProtobufClass.class)
					.define("description", pbChannelDataID.desc())
					.build()
			).make()
			.load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

			protocolIds.add(pbChannelDataID.ID());
			mapping.put(clazz, pbChannelDataID.ID());
		}
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	public static int protocolId(Class<? extends IpbChannelData> clazz) {
		return mapping.get(clazz);
	}
}
