package org.qiunet.utils.async.factory;

import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
	private final AtomicInteger threadNum = new AtomicInteger();
	private final Constructor<?> fastClzConstructor;
	private final String prefixName;
	public DefaultThreadFactory(String poolName){
		Constructor<?> fastClzConstructor1 = null;
		Class<?> fastClz;
		this.prefixName = poolName + "-thread-";
		try {
			fastClz = Class.forName("io.netty.util.concurrent.FastThreadLocalThread");
		} catch (ClassNotFoundException e) {
			fastClz = null;
		}
		if (fastClz != null) {
			try {
				fastClzConstructor1 = fastClz.getDeclaredConstructor(Runnable.class, String.class);
				fastClzConstructor1.setAccessible(true);
			} catch (NoSuchMethodException ignored) {
			}
		}
		this.fastClzConstructor = fastClzConstructor1;
	}

	@Override
	public Thread newThread(Runnable r) {
		Preconditions.checkNotNull(r);

		Thread localThread;
		String threadName = this.prefixName + this.threadNum.getAndIncrement();
		if (fastClzConstructor == null) {
			localThread = new Thread(r, threadName);
		}else {
			try {
				localThread = (Thread) fastClzConstructor.newInstance(r, threadName);
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		localThread.setDaemon(true);
		localThread.setPriority(5);
		return localThread;
	}

}
