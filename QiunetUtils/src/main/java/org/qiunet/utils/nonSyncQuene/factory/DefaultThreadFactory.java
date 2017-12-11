package org.qiunet.utils.nonSyncQuene.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
	private AtomicInteger threadNum;
	private AtomicInteger poolNum;
	private ThreadGroup group;
	private String prefixName;
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
		if (localThread.isDaemon()){
			localThread.setDaemon(true);
		}
		if (localThread.getPriority() != 5){
			localThread.setPriority(5);
		}
		return localThread;
	}

}
