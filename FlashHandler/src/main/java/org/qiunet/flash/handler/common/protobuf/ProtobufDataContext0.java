package org.qiunet.flash.handler.common.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.base.Preconditions;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 11:34
 */
class ProtobufDataContext0 implements IApplicationContextAware {
	private final Map<Class, Codec> codecMap = new HashMap<>();
	private static ProtobufDataContext0 instance;
	private IApplicationContext context;

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;
		this.handlerCodec();
		instance = this;
	}

	static ProtobufDataContext0 getInstance() {
		return instance;
	}

	private void handlerCodec() {
		Set<Class<? extends IProtobufClass>> classes0 = this.context.getSubTypesOf(IProtobufClass.class);
		Set<Class<?>> classes = this.context.getTypesAnnotatedWith(ProtobufClass.class);
		classes.addAll(classes0);
		for (Class<?> clazz : classes) {
			if (clazz.isInterface()
				|| clazz.isEnum()
				|| Modifier.isAbstract(clazz.getModifiers())
				|| ! Modifier.isPublic(clazz.getModifiers())
			) {
				continue;
			}
			try {
				codecMap.put(clazz, ProtobufProxy.create(clazz));
			}catch (Exception e) {
				throw new CustomException(e, "Protobuf create codec [{}] error", clazz.getName());
			}
		}
	}

	public <T> Codec<T> codec(Class<T> clazz) {
		Preconditions.checkState(codecMap.containsKey(clazz), "Have no codec for class [%s] !", clazz.getName());
		return codecMap.get(clazz);
	}
}
