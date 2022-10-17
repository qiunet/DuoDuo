package org.qiunet.flash.handler.common.observer;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.utils.string.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/***
 *
 * 观察者对象操作类.
 * 一般存放到被观察者对象上.
 *
 * @author qiunet
 * 2020-08-31 08:15
 **/
public final class ObserverSupport<Owner extends MessageHandler<Owner>> {
	private final Map<Class<? extends IObserver>, ObserverList<? extends IObserver>> observerMaps = Maps.newConcurrentMap();
	/**
	 * 根据名称缓存的监听组.
	 */
	private final Map<String, List<Observer<? extends IObserver>>> observerGroups = Maps.newConcurrentMap();

	private final AtomicInteger versions = new AtomicInteger();
	private final Owner owner;

	public ObserverSupport(Owner owner) {
		this.owner = owner;
	}

	/**
	 * 往该support添加一个观察者.
	 * @param o
	 * @param <O>
	 * @return
	 */
	public <O extends IObserver> Observer<O> attach(Class<O> clazz, O o) {
		return this.attach(null, clazz, o);
	}

	/**
	 * 将observer 加入group 组
	 * @param group 组名
	 * @param clazz observer class name
	 * @param o observer 实例
	 * @return Observer 对象
	 * @param <O>
	 */
	public <O extends IObserver> Observer<O> attach(String group, Class<O> clazz, O o) {
		return this.attach(group, clazz, o, false);
	}
	/**
	 * 往该support添加一个一次观察者.
	 * @param o
	 * @param <O>
	 * @return
	 */
	public <O extends IObserver> Observer<O> once(Class<O> clazz, O o) {
		return this.attach(null, clazz, o);
	}

	/**
	 * 往该support添加一个一次观察者.
	 * 并指定组
	 * @param group 组名
	 * @param clazz
	 * @param o
	 * @return
	 * @param <O>
	 */
	public <O extends IObserver> Observer<O> once(String group, Class<O> clazz, O o) {
		return this.attach(group, clazz, o, true);
	}

	/**
	 * attach
	 */
	private <O extends IObserver> Observer<O> attach(String group, Class<O> clazz, O o, boolean once) {
		ObserverList<O> observers = this.computeIfAbsent(clazz);
		Observer<O> observer = new Observer<>(group, clazz, this, o, versions.getAndIncrement(), once);
		observers.getObservers().add(observer);
		if (group != null) {
			this.computeIfAbsent(group).add(observer);
		}
		return observer;
	}

	/***
	 * 移除观察者
	 * @param observer
	 * @return
	 */
	public boolean remove(Observer observer) {
		if (observer == null) {
			return false;
		}

		boolean remove = this.computeIfAbsent(observer.getObserverClz()).remove(observer);
		if (observer.getGroup() != null && remove) {
			this.observerGroups.get(observer.getGroup()).remove(observer);
		}
		return remove;
	}

	/***
	 * 移除某一类观察者
	 * @param
	 */
	public void remove(Class<? extends IObserver> observerType) {
		this.observerMaps.get(observerType).forEach(this::remove);
	}

	/**
	 * 移除某一个组的观察者
	 * @param group 组名
	 */
	public void remove(String group) {
		this.foreachByGroup(group, this::remove);
	}

	/**
	 * 对某个group的Observer 进行操作
	 * @param group 组名
	 * @param consumer
	 */
	public void foreachByGroup(String group, Consumer<Observer<? extends IObserver>> consumer) {
		if (! this.observerGroups.containsKey(group)) {
			return;
		}
		this.computeIfAbsent(group).forEach(consumer);
	}

	/**
	 * 清除该support的所有观察者
	 */
	public void clear(){
		this.observerMaps.clear();
	}
	/**
	 * 清除该support的所有观察者
	 */
	public void clear(Predicate<Class<?>> filter){
		this.observerMaps.entrySet().removeIf(entry -> filter.test(entry.getKey()));
	}
	/**
	 * 根据class返回所有观察者
	 * @param clazz
	 * @return
	 */
	protected <O extends IObserver> ObserverList<O> computeIfAbsent(Class<O> clazz){
		return (ObserverList<O>) observerMaps.computeIfAbsent(clazz, key-> new ObserverList<O>());
	}

	/**
	 *
	 * @param group
	 * @return
	 */
	protected List<Observer<? extends IObserver>> computeIfAbsent(String group){
		if (StringUtil.isEmpty(group)) {
			return Collections.emptyList();
		}
		return observerGroups.computeIfAbsent(group, k -> new CopyOnWriteArrayList<>());
	}

	/**
	 * 异步触发监听
	 * @param clazz
	 * @param consumer
	 * @param <O>
	 */
	public <O extends IObserver> void asyncFire(Class<O> clazz, Consumer<O> consumer) {
		this.owner.addMessage(o -> syncFire(clazz, consumer));
	}
	/**
	 * 同步触发监听
	 * @param clazz
	 * @param consumer
	 * @param <O>
	 */
	public <O extends IObserver> void syncFire(Class<O> clazz, Consumer<O> consumer) {
		ObserverList<O> observers = this.computeIfAbsent(clazz);
		observers.forEach(o -> {
			if (o.isOnce()) {this.remove(o);}
			consumer.accept(o.getObserver());
		});
	}

	protected static class ObserverList<O extends IObserver> {
		private final List<Observer<O>> observers;

		ObserverList() {
			this.observers = new CopyOnWriteArrayList<>();
		}

		boolean remove(Observer<O> oObserver) {
			return this.observers.remove(oObserver);
		}

		void forEach(Consumer<Observer<O>> consumer) {
			observers.forEach(consumer);
		}

		List<Observer<O>> getObservers() {
			return observers;
		}
	}
}
