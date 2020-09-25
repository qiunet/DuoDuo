package org.qiunet.listener.observer;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/***
 *
 * 观察者对象操作类.
 * 一般存放到被观察者对象上.
 *
 * @author qiunet
 * 2020-08-31 08:15
 **/
public class ObserverSupport {

	private Map<Class<? extends IObserver>, ObserverList<? extends IObserver>> observerMaps = Maps.newHashMap();

	/**
	 * 往该support添加一个观察者.
	 * @param o
	 * @param <O>
	 * @return
	 */
	public <O extends IObserver> Observer<O> attach(Class<O> clazz, O o) {
		ObserverList<O> observers = this.computeIfAbsent(clazz);
		Observer<O> observer = new Observer<>(this, o);
		observers.getObservers().add(observer);
		return observer;
	}

	/***
	 * 移除观察者
	 * @param observer
	 * @return
	 */
	public boolean remove(Observer observer) {
		return this.computeIfAbsent(observer.getObserver().getClass()).remove(observer);
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
	 * 根据class返回所有观察者
	 * @param clazz
	 * @return
	 */
	private <O extends IObserver> ObserverList<O> computeIfAbsent(Class<O> clazz){
		return (ObserverList<O>) observerMaps.computeIfAbsent(clazz, key-> new ObserverList<O>());
	}

	/**
	 * 触发监听
	 * @param clazz
	 * @param consumer
	 * @param <O>
	 */
	public <O extends IObserver> void fire(Class<O> clazz, Consumer<O> consumer) {
		ObserverList<O> observers = this.computeIfAbsent(clazz);
		observers.forEach(o -> consumer.accept(o.getObserver()));
	}

	private static class ObserverList<O extends IObserver> {
		private List<Observer<O>> observers;

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
