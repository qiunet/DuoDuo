package org.qiunet.cross.transaction;

import com.google.common.collect.Maps;
import org.qiunet.cross.rpc.IRpcRequest;
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
class TransactionManager0 implements IApplicationContextAware {

	private static final Map<Class, ITransactionHandler> handles = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends ITransactionHandler>> classes = context.getSubTypesOf(ITransactionHandler.class);
		for (Class<? extends ITransactionHandler> clazz : classes) {
			Method method = clazz.getMethod("handler", DTransaction.class);
			ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
			Class requestClass = (Class) type.getActualTypeArguments()[0];
			if (handles.containsKey(requestClass)) {
				throw new CustomException("Request Class [{}] in transaction handles is repeated!", requestClass.getName());
			}
			Class responseClass = (Class) type.getActualTypeArguments()[1];
			// 检查默认构造函数
			Constructor<? extends IRpcRequest> constructorReq = requestClass.getDeclaredConstructor();
			Constructor<? extends IRpcRequest> constructorRsp = responseClass.getDeclaredConstructor();

			handles.put(requestClass, (ITransactionHandler)context.getInstanceOfClass(clazz));
		}
	}

	/**
	 * 处理事务
	 * @param reqClass
	 * @param transaction
	 */
	static void handler(Class reqClass, DTransaction transaction) {
		handles.get(reqClass).handler(transaction);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.TRANSACTION;
	}

	@Override
	public int order() {
		return -1;
	}
}
