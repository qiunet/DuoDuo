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
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/***
 * 监听器的manager
 *
 * @author qiunet
 */
enum EventManager0 implements IApplicationContextAware {
	instance;
	private final Map<Class<? extends IListenerEvent>, List<EventSubscriber>> listeners = new HashMap<>();
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
			Class<? extends IListenerEvent> eventDataClass = eventDataClass(method);
			List<EventSubscriber> subscriberList = this.listeners.computeIfAbsent(eventDataClass, key -> Lists.newArrayList());
			subscriberList.add(wrapper(method));
		}

		this.listeners.values().forEach(list -> list.sort((o1, o2) -> ComparisonChain.start().compare(o2.weight, o1.weight)
			.compareTrueFirst(o1.selfMethod, o2.selfMethod) // 同等weight, DuoDuo包优先处理
			.result()));
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
	private Class<? extends IListenerEvent> eventDataClass(Method method) {
		Preconditions.checkArgument(method.getParameterCount() == 1, "EventListener Method parameter count %s error!", method.getParameterCount());
		Preconditions.checkArgument(IListenerEvent.class.isAssignableFrom(method.getParameterTypes()[0]), "EventListener Method parameter must be IEventData");
		return (Class<? extends IListenerEvent>) method.getParameterTypes()[0];
	}

	/**
	 * 使用eventData method 构造一个Wrapper
	 * @param method
	 * @return
	 */
	private EventSubscriber wrapper(Method method) {
		EventListener annotation = method.getAnnotation(EventListener.class);
		Object implInstance = null;
		// 允许静态方法
		if (! Modifier.isStatic(method.getModifiers())) {
			implInstance = context.getInstanceOfClass(method.getDeclaringClass());
		}
		return new EventSubscriber(implInstance, method, annotation.value().ordinal(), annotation.limitCount());
	}

	/***
	 * 触发事件
	 * @param event 事件数据
	 */
	void post(IListenerEvent event) {
		this.post(event, (m, e) -> {
			if (e instanceof IllegalAccessException) {
				throw new CustomException(e, "Fire Event Handler [{}.{}] Error!", m.getDeclaringClass().getName(), m.getName());
			}
			else if (e instanceof InvocationTargetException) {
				Throwable targetException = ((InvocationTargetException) e).getTargetException();
				if (! (targetException instanceof CustomException)) {
					throw new CustomException(targetException, "Fire Event Handler [{}.{}] Error!", m.getDeclaringClass().getName(), m.getName());
				}else {
					throw ((CustomException) targetException);
				}
			}else {
				throw new CustomException(e, "Fire Event Error!");
			}
		});
	}

	/**
	 * 触发事件 自己处理异常
	 * @param event 事件数据
	 * @param exConsume 异常消费
	 */
	void post(IListenerEvent event, BiConsumer<Method, Throwable> exConsume) {
		if (! inited.get()) {
			throw new CustomException("Event not init");
		}

		List<EventSubscriber> wrappers = listeners.get(event.getClass());
		if (wrappers == null) {
			return;
		}

		wrappers.forEach(w -> w.handleEvent(event, exConsume));
	}

	private static class EventSubscriber {
		private final int weight;
		private final Method method;
		private final Object caller;
		private final int limitCount;
		private final boolean selfMethod;
		private int currCount;

		private EventSubscriber(Object caller, Method method, int weight, int limitCount) {
			this.selfMethod = method.getDeclaringClass().getName().startsWith("org.qiunet");
			this.caller = caller;
			this.weight = weight;
			this.method = method;
			this.limitCount = limitCount;
			method.setAccessible(true);
		}

		void handleEvent(IListenerEvent data, BiConsumer<Method, Throwable> exConsume) {
			if (this.limitCount != 0 && (currCount >= this.limitCount || (currCount++) >= this.limitCount)) {
				return;
			}

			try {
				method.invoke(caller, data);
			} catch (Throwable e) {
				exConsume.accept(method, e);
			}
		}
	}
}
