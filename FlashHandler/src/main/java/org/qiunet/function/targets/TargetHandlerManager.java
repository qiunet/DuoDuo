package org.qiunet.function.targets;

import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:18
 */
enum TargetHandlerManager implements IApplicationContextAware {
	instance;

	private Map<ITargetType, BaseTargetHandler> handlerMap;

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends BaseTargetHandler>> classes = context.getSubTypesOf(BaseTargetHandler.class);
		handlerMap = Maps.newHashMapWithExpectedSize(classes.size());
		for (Class<? extends BaseTargetHandler> clazz : classes) {
			if (Modifier.isAbstract(clazz.getModifiers())) {
				continue;
			}

			BaseTargetHandler obj = (BaseTargetHandler) context.getInstanceOfClass(clazz);
			BaseTargetHandler old = handlerMap.put((ITargetType) obj.getType(), obj);
			if (old != null) {
				throw new CustomException("Type {} TargetHandler is repeated", obj.getType());
			}
		}
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.TARGET_HANDLER;
	}

	/**
	 * 根据类型获得一个handler
	 * @param type
	 * @return
	 */
	BaseTargetHandler getHandler(ITargetType type) {
		return handlerMap.get(type);
	}
}
