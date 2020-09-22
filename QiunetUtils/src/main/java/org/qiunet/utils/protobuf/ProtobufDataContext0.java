package org.qiunet.utils.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.base.Preconditions;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

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
		this.context.getTypesAnnotatedWith(ProtobufClass.class)
			.forEach(clazz -> {
				if (Modifier.isInterface(clazz.getModifiers())
				|| Modifier.isAbstract(clazz.getModifiers())
				|| ! Modifier.isPublic(clazz.getModifiers())
				) {
					return;
				}

				codecMap.put(clazz, ProtobufProxy.create(clazz));
			});
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
