package org.qiunet.flash.handler.common.observer;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.MessageHandler;

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
	private final Owner owner;
	private final AtomicInteger versions = new AtomicInteger();
	private final Map<Class<? extends IObserver>, ObserverList<? extends IObserver>> observerMaps = Maps.newConcurrentMap();

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
		return this.attach(clazz, o, false);
	}
	/**
	 * 往该support添加一个观察者.
	 * @param o
	 * @param <O>
	 * @return
	 */
	public <O extends IObserver> Observer<O> once(Class<O> clazz, O o) {
		return this.attach(clazz, o, true);
	}

	/**
	 * attach
	 */
	private <O extends IObserver> Observer<O> attach(Class<O> clazz, O o, boolean once) {
		ObserverList<O> observers = this.computeIfAbsent(clazz);
		Observer<O> observer = new Observer<>(clazz, this, o, versions.getAndIncrement(), once);
		observers.getObservers().add(observer);
		return observer;
	}

	/***
	 * 移除观察者
	 * @param observer
	 * @return
	 */
	public boolean remove(Observer observer) {
		return this.computeIfAbsent(observer.getObserverClz()).remove(observer);
	}

	/***
	 * 移除某一类观察者
	 * @param
	 */
	public void remove(Class<? extends IObserver> observerType) {
		this.observerMaps.remove(observerType);
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
