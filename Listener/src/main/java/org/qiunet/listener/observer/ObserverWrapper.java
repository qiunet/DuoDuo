package org.qiunet.listener.observer;

/***
 * 观察者的包装类.
 *
 * @author qiunet
 * 2020-08-31 08:19
 **/
public class ObserverWrapper {

	private IObserver observer;

	public ObserverWrapper(IObserver observer) {
		this.observer = observer;
	}

	public IObserver getObserver() {
		return observer;
	}
}
