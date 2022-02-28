package org.qiunet.flash.handler.common.observer;

/***
 * 观察者的包装类.
 *
 * @author qiunet
 * 2020-08-31 08:19
 **/
public class Observer<O extends IObserver> {
	private final ObserverSupport support;
	/**
	 * 保证一个执行顺序. fire时候. 只fire触发前添加的. 后面attach的. 就不再触发.
	 */
	private final int version;
	private final O observer;
	private final boolean once;

	Observer(ObserverSupport support, O observer, int version, boolean once) {
		this.observer = observer;
		this.version = version;
		this.support = support;
		this.once = once;
	}

	/**
	 * 移除自身
	 * @return
	 */
	public boolean remove() {
		return support.remove(this);
	}

	public int getVersion() {
		return version;
	}

	public boolean isOnce() {
		return once;
	}

	/**
	 *获得里面的真实监听者
	 * @return
	 */
	O getObserver() {
		return observer;
	}
}
