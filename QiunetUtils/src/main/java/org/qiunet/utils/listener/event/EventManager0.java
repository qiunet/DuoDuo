package org.qiunet.utils.listener.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 监听器的manager
 *
 * @author qiunet
 */
enum EventManager0 implements IApplicationContextAware {
	instance;
	private final Map<Class<? extends IEventData>, List<EventSubscriber>> listeners = new HashMap<>();
	private final AtomicBoolean inited = new AtomicBoolean();
	private IApplicationContext context;

	static EventManager0 getInstance() {
		return instance;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		if (! inited.compareAndSet(false, true)) {
			return;
		}

		this.context = context;
		Set<Method> typesAnnotated = context.getMethodsAnnotatedWith(EventListener.class);
		for (Method method : typesAnnotated) {
			Class<? extends IEventData> eventDataClass = eventDataClass(method);
			List<EventSubscriber> subscriberList = this.listeners.computeIfAbsent(eventDataClass, key -> Lists.newArrayList());
			subscriberList.add(wrapper(method));
		}

		this.listeners.values().forEach(list -> list.sort((o1, o2) -> ComparisonChain.start().compare(o2.weight, o1.weight).result()));
		LoggerType.DUODUO.debug("EventManager find {} event!", this.listeners.size());
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.EVENT;
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE - 2;
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
	private EventSubscriber wrapper(Method method) {
		EventListener annotation = method.getAnnotation(EventListener.class);
		Object implInstance = context.getInstanceOfClass(method.getDeclaringClass());
		return new EventSubscriber(implInstance, method, annotation.value().ordinal(), annotation.limitCount());
	}

	/***
	 *
	 * @param eventData
	 */
	void post(IEventData eventData) {
		if (! inited.get()) {
			throw new CustomException("Event not init");
		}

		List<EventSubscriber> wrappers = listeners.get(eventData.getClass());
		if (wrappers == null) {
			return;
		}

		wrappers.forEach(w -> w.handleEvent(eventData));
	}

	private static class EventSubscriber {
		private final int weight;
		private final Method method;
		private final Object caller;
		private final int limitCount;
		private int currCount;

		private EventSubscriber(Object caller, Method method, int weight, int limitCount) {
			this.caller = caller;
			this.weight = weight;
			this.method = method;
			this.limitCount = limitCount;
			method.setAccessible(true);
		}

		void handleEvent(IEventData data) {
			if (this.limitCount != 0 && (currCount >= this.limitCount || (currCount++) >= this.limitCount)) {
				return;
			}

			try {
				method.invoke(caller, data);
			} catch (IllegalAccessException e) {
				throw new CustomException(e, "Fire Event Handler [{}.{}] Error!", caller.getClass().getName(), method.getName());
			} catch (InvocationTargetException e) {
				if (!(e.getTargetException() instanceof CustomException)) {
					throw new CustomException(e.getTargetException(), "Fire Event Handler [{}.{}] Error!", caller.getClass().getName(), method.getName());
				}else {
					throw ((CustomException) e.getTargetException());
				}
			}
		}
	}
}
