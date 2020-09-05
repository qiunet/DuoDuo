package org.qiunet.listener.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 监听器的manager
 *
 * @author qiunet
 */
class EventManager0 implements IApplicationContextAware {
	private Map<Class<? extends IEventData>, List<Wrapper>> listeners = new HashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(EventManager.class);
	private static EventManager0 instance;
	private IApplicationContext context;
	private EventManager0(){
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
		instance = this;
	}

	static EventManager0 getInstance() {
		return instance;
	}

	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;

		Set<Method> typesAnnotated = context.getMethodsAnnotatedWith(EventListener.class);
		for (Method method : typesAnnotated) {
			Class<? extends IEventData> eventDataClass = eventDataClass(method);
			List<Wrapper> wrapperList = this.listeners.computeIfAbsent(eventDataClass, key -> Lists.newArrayList());
			wrapperList.add(wrapper(method));
		}

		this.listeners.values().forEach(list -> list.sort((o1, o2) -> ComparisonChain.start().compare(o2.weight, o1.weight).result()));
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 获得eventData的class
	 * @param method
	 * @return
	 */
	private Class<? extends IEventData> eventDataClass(Method method) {
		Preconditions.checkArgument(method.getParameterCount() == 1, "EventListener Method parameter count %s error!", method.getParameterCount());
		Preconditions.checkArgument(IEventData.class.isAssignableFrom(method.getParameterTypes()[0]), "EventListener Method parameter must be IEventData");
		return (Class<? extends IEventData>) method.getParameterTypes()[0];
	}

	/**
	 * 使用eventData method 构造一个Wrapper
	 * @param method
	 * @return
	 */
	private Wrapper wrapper(Method method) {
		EventListener annotation = method.getAnnotation(EventListener.class);
		Object implInstance = context.getInstanceOfClass(method.getDeclaringClass());
		return new Wrapper(implInstance, method, annotation.value().ordinal());
	}

	/***
	 *
	 * @param eventData
	 */
	void fireEventHandler(IEventData eventData) {
		List<Wrapper> wrappers = listeners.get(eventData.getClass());
		if (wrappers == null) {
			throw new NullPointerException("No listener for class ["+eventData.getClass().getName()+"]");
		}

		wrappers.forEach(w -> w.fireEventHandler(eventData));
	}

	private static class Wrapper {
		private int weight;
		private Method method;
		private Object caller;

		private Wrapper(Object caller, Method method, int weight) {
			this.caller = caller;
			this.weight = weight;
			this.method = method;
			method.setAccessible(true);
		}

		void fireEventHandler(IEventData data) {
			try {
				method.invoke(caller, data);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("Fire Event Handler Error: ", e);
			}
		}
	}
}
