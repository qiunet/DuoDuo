package org.qiunet.utils.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.base.Preconditions;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.string.StringUtil;

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
	private Map<Class, Codec> codecMap = new HashMap<>();
	private static ProtobufDataContext0 instance;
	private IApplicationContext context;

	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;
		this.handlerCodec();
		instance = this;
	}

	static ProtobufDataContext0 getInstance() {
		return instance;
	}

	private void handlerCodec() {
		Set<Class<?>> classes = this.context.getTypesAnnotatedWith(ProtobufClass.class);
		for (Class<?> clazz : classes) {
			if (clazz.isInterface()
				|| Modifier.isAbstract(clazz.getModifiers())
				|| ! Modifier.isPublic(clazz.getModifiers())
				|| clazz.isEnum()
			) {
				continue;
			}

			ProtobufClass protobufClass = clazz.getAnnotation(ProtobufClass.class);
			if (StringUtil.isEmpty(protobufClass.description())) {
				throw new IllegalArgumentException("class ["+clazz.getName()+"] need specify description to ProtobufClass annotation!");
			}

			codecMap.put(clazz, ProtobufProxy.create(clazz));
		}
	}

	public <T> Codec<T> codec(Class<T> clazz) {
		Preconditions.checkState(codecMap.containsKey(clazz), "Have no codec for class [%s] !", clazz.getName());
		return codecMap.get(clazz);
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE - 3;
	}
}
