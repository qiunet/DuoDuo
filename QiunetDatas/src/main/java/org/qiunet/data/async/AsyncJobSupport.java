package org.qiunet.data.async;

import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.nonSyncQuene.mutiThread.DefaultExecutorRejectHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 异步更新的公用类
 * @author qiunet
 *         Created on 17/2/11 08:04.
 */
public class AsyncJobSupport {
	private ExecutorService executor = new ThreadPoolExecutor(
			10,
			512,
			60,
			TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(1024),
			new DefaultThreadFactory("AsyncJobSupport"),
			new DefaultExecutorRejectHandler("AsyncJobSupport"));

	private volatile static AsyncJobSupport instance;

	private AsyncJobSupport() {
		instance = this;
	}

	public static AsyncJobSupport getInstance() {
		if (instance == null) {
			synchronized (AsyncJobSupport.class) {
				if (instance == null)
				{
					new AsyncJobSupport();
				}
			}
		}
		return instance;
	}

	private Set<AsyncNode> nodes = new HashSet<>();

	public void addNode(AsyncNode node) {
		this.nodes.add(node);
	}

	/***
	 * 停止线程池
	 */
	public void shutdown(){
		executor.shutdown();
	}

	/***
	 * 异步更新到db
	 */
	public void asyncToDb(){
		for (final AsyncNode node : nodes) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// 必须try catch 否则导致线程停止
						node.updateRedisDataToDatabase();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
