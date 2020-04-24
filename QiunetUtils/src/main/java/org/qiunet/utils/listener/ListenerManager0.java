package org.qiunet.utils.listener;

import com.google.common.collect.ComparisonChain;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.classScanner.Singleton;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/***
 * 监听器的manager
 *
 * @author qiunet
 */
@Singleton
class ListenerManager0 implements IApplicationContextAware {
	private Map<Class<? extends IEventData>, List<Wrapper>> listeners = new HashMap<>();
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	private static ListenerManager0 instance;
	private IApplicationContext context;
	private ListenerManager0(){
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
		instance = this;
	}

	static ListenerManager0 getInstance() {
		return instance;
	}

//	private static final Collector<Wrapper, List<Wrapper>, List<Wrapper>> sortList = Collector.of(
//		ArrayList::new,
//		List::add,
//		(left, right)->{left.addAll(right); return left;},
//		i -> {i.sort((o1, o2) -> ComparisonChain.start().compare(o2.weight, o1.weight).result()); return i;});


	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;

		Set<Class<? extends IEventData>> eventDataClasses = context.getSubTypesOf(IEventData.class);
		for (Class<? extends IEventData> eventDataClass : eventDataClasses) {
			this.listeners.put(eventDataClass, wrapperList(eventDataClass));
		}
	}

	/**
	 * 使用eventDataClass 构造一个
	 * @param eventDataClass
	 * @return
	 */
	private List<Wrapper> wrapperList(Class<?> eventDataClass) throws NoSuchMethodException {
		EventListener annotation = eventDataClass.getAnnotation(EventListener.class);
		if (annotation == null) {
			throw new NullPointerException("Class ["+eventDataClass.getName()+"] need specify EventListener annotation!");
		}

		String methodName = getEventListenerMethodName(annotation.value(), eventDataClass);
		Set<Class<?>> impls = context.getSubTypesOf((Class<Object>) annotation.value());
		List<Wrapper> wrappers = new ArrayList<>(impls.size());
		for (Class<?> impl : impls) {
			Object implInstance = context.getInstanceOfClass(impl);
			Method callerMethod = impl.getDeclaredMethod(methodName, eventDataClass);
			EventHandlerWeight handlerWeight = callerMethod.getAnnotation(EventHandlerWeight.class);
			Wrapper wrapper = new Wrapper(implInstance, callerMethod, handlerWeight == null ?
				0 : handlerWeight.value().ordinal());
			wrappers.add(wrapper);
		}
		wrappers.sort((o1, o2) -> ComparisonChain.start().compare(o2.weight, o1.weight).result());
		return wrappers;
	}

	/**
	 * 获取eventListener 的接口名称.
	 * @param eventListenerClass eventListener 的class
	 * @param eventDataClass 使用该注解的eventData class
	 * @return 接口中, 包含该eventData参数的方法名.
	 */
	private String getEventListenerMethodName(Class<?> eventListenerClass, Class<?> eventDataClass) {
		for (Method method : eventListenerClass.getDeclaredMethods()) {
			if (method.getParameterCount() != 1) {
				continue;
			}

			if (method.getParameterTypes()[0] != eventDataClass) {
				continue;
			}

			return method.getName();
		}
		throw new NullPointerException("EventListener ["+eventListenerClass.getName()+"] not define method use parameter ["+eventDataClass.getName()+"]");
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
