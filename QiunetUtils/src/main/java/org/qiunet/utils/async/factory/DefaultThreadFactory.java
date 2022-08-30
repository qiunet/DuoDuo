package org.qiunet.utils.async.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
	private final AtomicInteger threadNum;
	private final AtomicInteger poolNum;
	private final ThreadGroup group;
	private final String prefixName;
	public DefaultThreadFactory(String poolName){
		this.threadNum = new AtomicInteger();
		this.poolNum = new AtomicInteger();
		this.prefixName = poolName +"-" + poolNum.getAndIncrement()+ "-threaed-";
		SecurityManager localSecurityManager = System.getSecurityManager();
		this.group = (localSecurityManager != null ? localSecurityManager.getThreadGroup() : Thread.currentThread().getThreadGroup());
	}

	@Override
	public Thread newThread(Runnable paramRunnable) {
		Thread localThread = new Thread(this.group, paramRunnable,this.prefixName + this.threadNum.getAndIncrement(), 0L);
		localThread.setDaemon(true);
		localThread.setPriority(5);
		return localThread;
	}

}
