package org.qiunet.cross.transaction;

import com.google.common.collect.Maps;
import org.qiunet.data.util.DbProperties;
import org.qiunet.data.util.ServerType;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

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
		if (DbProperties.getInstance().getServerType() == ServerType.ALL) {
			return;
		}

		Set<Class<? extends ITransactionHandler>> classes = context.getSubTypesOf(ITransactionHandler.class);
		for (Class<? extends ITransactionHandler> clazz : classes) {
			Method method = clazz.getMethod("handler", DTransaction.class);
			ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
			handles.put((Class)type.getActualTypeArguments()[0], (ITransactionHandler)context.getInstanceOfClass(clazz));
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
}