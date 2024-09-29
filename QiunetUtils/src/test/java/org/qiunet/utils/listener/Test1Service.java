package org.qiunet.utils.listener;


public enum Test1Service {
	instance;
	@EventListener
	public void eventHandler(Test1EventData eventData) {
			TestListener.test1Count.incrementAndGet();
	}
}
