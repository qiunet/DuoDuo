package org.qiunet.cross.transaction;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.util.ServerType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

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
enum TransactionManager0 implements IApplicationContextAware {
	instance;

	private Map<Class, ITransactionHandler> handles = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		if (ServerNodeManager.getCurrServerType() == ServerType.ALL) {
			return;
		}

		Set<Class<? extends ITransactionHandler>> classes = context.getSubTypesOf(ITransactionHandler.class);
		for (Class<? extends ITransactionHandler> clazz : classes) {
			Method method = clazz.getMethod("handler", DTransaction.class);
			ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
			Class requestClass = (Class) type.getActualTypeArguments()[0];
			if (handles.containsKey(requestClass)) {
				throw new CustomException("Request Class [{}] in transaction handles is repeated!", requestClass.getName());
			}
			handles.put(requestClass, (ITransactionHandler)context.getInstanceOfClass(clazz));
		}

		Set<Class<? extends CrossData>> subTypesOf = context.getSubTypesOf(CrossData.class);
		Set<String> classNames = Sets.newHashSet();
		for (Class<? extends CrossData> aClass : subTypesOf) {
			String realClass = aClass.getName().substring(0, aClass.getName().indexOf("$"));
			if (classNames.contains(realClass)) {
				continue;
			}
			Class.forName(realClass);
			classNames.add(realClass);
		}
	}

	/**
	 * 处理事务
	 * @param reqClass
	 * @param transaction
	 */
	void handler(Class reqClass, DTransaction transaction) {
		handles.get(reqClass).handler(transaction);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.SERVER;
	}

	@Override
	public int order() {
		return -1;
	}
}
