package org.qiunet.utils.listener;

import com.google.common.collect.ComparisonChain;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ListenerManager0 implements IApplicationContextAware {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private static ListenerManager0 instance;
	private Map<Class<? extends IEventData>, List<Wrapper>> methods;
	private ListenerManager0(){
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	static ListenerManager0 getInstance() {
		return instance;
	}

	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Method> annotated = context.getMethodsAnnotatedWith(_EventHandlers.class);
		annotated.addAll(context.getMethodsAnnotatedWith(EventHandler.class));

		Collector<Wrapper, List<Wrapper>, List<Wrapper>> sortList = Collector.of(
			ArrayList::new,
			List::add,
			(left, right)->{left.addAll(right); return left;},
			i -> {i.sort((o1, o2) -> ComparisonChain.start().compare(o2.weight, o1.weight).result()); return i;});

		methods = annotated.stream()
			.filter(m -> IEventListener.class.isAssignableFrom(m.getDeclaringClass()))
			.map(m ->new Wrapper((IEventListener) context.getInstanceOfClass(m.getDeclaringClass()), m))
			.flatMap(Wrapper::flatMap)
			.collect(Collectors.groupingBy(Wrapper::getDataClass, sortList));
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
		private int weight;
		private Method method;
		private IEventListener caller;
		private Class<? extends IEventData> dataClass;

		public Wrapper(IEventListener caller, Method method) {
			this.caller = caller;
			this.method = method;
		}

		private Wrapper(Class<? extends IEventData> dataClass, IEventListener caller, Method method, int weight) {
			this.caller = caller;
			this.weight = weight;
			this.method = method;
			this.dataClass = dataClass;
		}

		Stream<Wrapper> flatMap(){
			Map<Class<? extends IEventData>, Integer> as = new HashMap<>();
			_EventHandlers annotation = this.method.getAnnotation(_EventHandlers.class);
			if (annotation != null) {
				for (EventHandler eventHandler : annotation.value()) {
					as.merge(eventHandler.value(), eventHandler.weight(), Integer::sum);
				}
			}
			EventHandler eventHandler = this.method.getAnnotation(EventHandler.class);
			if (eventHandler != null) as.merge(eventHandler.value(), eventHandler.weight(), Integer::sum);

			return as.entrySet().stream()
				.map(en -> new Wrapper(en.getKey(), caller, method, en.getValue()));
		}

		public Class<? extends IEventData> getDataClass() {
			return dataClass;
		}

		void fireEventHandler(IEventData data) {
			caller.eventHandler(data);
		}
	}
}
