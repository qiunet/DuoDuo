package org.qiunet.listener.observer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
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

	private Map<Class<? extends IObserver>, List<Observer>> observerMaps = Maps.newHashMap();

	/**
	 * 往该support添加一个观察者.
	 * @param o
	 * @param <O>
	 * @return
	 */
	public <O extends IObserver> Observer<O> attach(Class<O> clazz, O o) {
		List<Observer> observers = this.computeIfAbsent(clazz);
		Observer<O> observer = new Observer<>(this, o);
		observers.add(observer);
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
	private List<Observer> computeIfAbsent(Class<? extends IObserver> clazz){
		return observerMaps.computeIfAbsent(clazz, key-> Lists.newCopyOnWriteArrayList());
	}

	/**
	 * 触发监听
	 * @param clazz
	 * @param consumer
	 * @param <O>
	 */
	public <O extends IObserver> void fire(Class<O> clazz, Consumer<O> consumer) {
		List<Observer> observers = this.computeIfAbsent(clazz);
		observers.forEach(o -> consumer.accept((O) o.getObserver()));
	}
}
