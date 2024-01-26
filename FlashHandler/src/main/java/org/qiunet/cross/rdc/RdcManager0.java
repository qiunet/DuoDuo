package org.qiunet.cross.rdc;

import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 18:05
 */
class RdcManager0 implements IApplicationContextAware {

	private static final Map<Class, IRdcHandler> handles = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends IRdcHandler>> classes = context.getSubTypesOf(IRdcHandler.class);
		for (Class<? extends IRdcHandler> clazz : classes) {
			Method method = clazz.getMethod("handler", DRdc.class);
			ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
			Class requestClass = (Class) type.getActualTypeArguments()[0];
			if (handles.containsKey(requestClass)) {
				throw new CustomException("Request Class [{}] in rdc handles is repeated!", requestClass.getName());
			}
			Class responseClass = (Class) type.getActualTypeArguments()[1];
			// 检查默认构造函数
			Constructor<? extends IRdcRequest> constructorReq = requestClass.getDeclaredConstructor();
			Constructor<? extends IRdcResponse> constructorRsp = responseClass.getDeclaredConstructor();

			handles.put(requestClass, (IRdcHandler)context.getInstanceOfClass(clazz));
		}
	}

	/**
	 * 处理远程数据调用
	 * @param reqClass
	 * @param rdc
	 */
	static void handler(Class reqClass, DRdc rdc) {
		handles.get(reqClass).handler(rdc);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.RDC;
	}

	@Override
	public int order() {
		return -1;
	}
}
