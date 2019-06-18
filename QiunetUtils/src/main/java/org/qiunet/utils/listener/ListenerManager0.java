package org.qiunet.utils.listener;

import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ListenerManager0 implements IApplicationContextAware {
	private static ListenerManager0 instance;
	private Map<Class<? extends IEventData>, List<Wrapper>> methods;
	private ListenerManager0(){
		instance = this;
	}

	static ListenerManager0 getInstance() {
		return instance;
	}

	@Override
	public void setApplicationContext(IApplicationContext context) {
		methods = context.getMethodsAnnotatedWith(EventHandler.class)
			.parallelStream().map(m -> {
				try {
					return new Wrapper(m.getDeclaringClass().newInstance(), m);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return null; })
			.filter(Objects::nonNull)
			.flatMap(Wrapper::flatMap)
			.collect(Collectors.groupingBy(Wrapper::getDataClass));
	}

	/***
	 *
	 * @param eventData
	 */
	void fireEventHandler(IEventData eventData) {
		List<Wrapper> wrappers = methods.get(eventData.getClass());
		if (wrappers == null) return;

		wrappers.forEach(w -> w.fireEventHandler(eventData));
	}

	private class Wrapper {
		private Object caller;
		private Method method;
		private Class<? extends IEventData> dataClass;

		public Wrapper(Object caller, Method method) {
			this.caller = caller;
			this.method = method;
		}

		private Wrapper(Class<? extends IEventData> dataClass, Object caller, Method method) {
			this.caller = caller;
			this.method = method;
			this.dataClass = dataClass;
		}

		Stream<Wrapper> flatMap(){
			return Stream.of(this.method.getAnnotation(EventHandler.class).value())
				.distinct().map(type -> new Wrapper(type, caller, method));
		}

		public Class<? extends IEventData> getDataClass() {
			return dataClass;
		}

		void fireEventHandler(IEventData data) {
			try {
				method.invoke(caller, data);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
