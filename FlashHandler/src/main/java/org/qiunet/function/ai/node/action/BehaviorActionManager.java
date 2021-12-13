package org.qiunet.function.ai.node.action;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.condition.Conditions;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/***
 *
 * @author qiunet
 * 2021/12/13 16:27
 */
public enum BehaviorActionManager {
	instance;
	private static final Map<String, Constructor<? extends IBehaviorAction>> actions = Maps.newHashMap();


	public <Obj> void createAction(String className, Obj obj, Conditions<Obj> conditions) {

	}

	private enum BehaviorActionManager0 implements IApplicationContextAware {
		instance;

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<? extends IBehaviorAction>> classSet = context.getSubTypesOf(IBehaviorAction.class);
			for (Class<? extends IBehaviorAction> aClass : classSet) {
				if (Modifier.isAbstract(aClass.getModifiers())) {
					continue;
				}

				if (! aClass.isAnnotationPresent(BehaviorAction.class)) {
					throw new CustomException("action [{}] need specify @BehaviorAction", aClass.getName());
				}

				Constructor<? extends IBehaviorAction> constructor = null;
				for (Constructor<?> c : aClass.getConstructors()) {
					if (c.getParameterCount() != 2) {
						continue;
					}

					if (IConditions.class != c.getParameterTypes()[1]) {
						continue;
					}

					if (! IMessageHandler.class.isAssignableFrom(c.getParameterTypes()[0])) {
						continue;
					}

					constructor = (Constructor<? extends IBehaviorAction>) c;
					break;
				}

				if (constructor == null) {
					throw new CustomException("action [{}] need a constructor with a messageHandler and a IConditions object", aClass.getName());
				}

				actions.put(aClass.getName(), constructor);
			}
		}
	}
}
