package org.qiunet.utils.async;


public class TestElement implements IQueueElement {
	private String threadName;
	public TestElement(String name){
		this.threadName = name;
	}
	@Override
	public boolean handler() {
		return true;
	}
	@Override
	public String toStr() {
		return "["+threadName+"]";
	}
}
