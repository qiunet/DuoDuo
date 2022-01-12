package org.qiunet.flash.handler.common.observer;

import org.qiunet.flash.handler.common.MessageHandler;

import java.util.function.Consumer;

/***
 *
 * @author qiunet
 * 2021/12/22 09:42
 */
public interface IObserverSupportOwner<Owner extends MessageHandler<Owner>> {
	/**
	 * 获取
	 * @return
	 */
	ObserverSupport<Owner> getObserverSupport();
	/**
	 * 往该support添加一个观察者.
	 * @param o
	 * @param <O>
	 * @return
	 */
	default <O extends IObserver> Observer<O> attachObserver(Class<O> clazz, O o) {
		return getObserverSupport().attach(clazz, o);
	}
	/***
	 * 移除观察者
	 * @param observer
	 * @return
	 */
	default boolean removeObserver(Observer observer) {
		return getObserverSupport().remove(observer);
	}

	/***
	 * 移除某一类观察者
	 * @param
	 */
	default void removeObserver(Class<? extends IObserver> observerType) {
		getObserverSupport().remove(observerType);
	}

	/**
	 * 清除该support的所有观察者
	 */
	default void clearObservers() {
		getObserverSupport().clear();
	}
	/**
	 * 异步触发监听
	 * @param clazz
	 * @param consumer
	 * @param <O>
	 */
	default  <O extends IObserver> void asyncFireObserver(Class<O> clazz, Consumer<O> consumer) {
		getObserverSupport().asyncFire(clazz, consumer);
	}

	/**
	 * 同步触发监听
	 * @param clazz
	 * @param consumer
	 * @param <O>
	 */
	default  <O extends IObserver> void syncFireObserver(Class<O> clazz, Consumer<O> consumer) {
		getObserverSupport().syncFire(clazz, consumer);
	}
}
