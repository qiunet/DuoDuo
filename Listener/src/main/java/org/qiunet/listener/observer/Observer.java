package org.qiunet.listener.observer;

/***
 * 观察者的包装类.
 *
 * @author qiunet
 * 2020-08-31 08:19
 **/
public class Observer<O extends IObserver> {
	private ObserverSupport support;
	private O observer;

	Observer(ObserverSupport support, O observer) {
		this.observer = observer;
		this.support = support;
	}

	/**
	 * 移除自身
	 * @return
	 */
	public boolean remove() {
		return support.remove(this);
	}

	/**
	 *获得里面的真实监听者
	 * @return
	 */
	O getObserver() {
		return observer;
	}
}
