package org.qiunet.utils.async.factory;

import com.google.common.base.Preconditions;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
	private final AtomicInteger threadNum = new AtomicInteger();
	private final String prefixName;
	public DefaultThreadFactory(String poolName){
		this.prefixName = poolName + "-thread-";
	}

	@Override
	public Thread newThread(Runnable r) {
		Preconditions.checkNotNull(r);

		Thread localThread = new Thread(r,this.prefixName + this.threadNum.getAndIncrement());
		localThread.setDaemon(true);
		localThread.setPriority(5);
		return localThread;
	}

}
