package org.qiunet.data.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步更新的公用类
 * @author qiunet
 *         Created on 17/2/11 08:04.
 */
public class AsyncJobSupport {
	private ExecutorService executor = new ThreadPoolExecutor(10, 50, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(30));

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

	private List<AsyncNode> nodes = new ArrayList<>();

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
		}
	}
}
