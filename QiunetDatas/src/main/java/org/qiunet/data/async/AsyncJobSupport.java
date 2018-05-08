package org.qiunet.data.async;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.asyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.asyncQuene.mutiThread.DefaultExecutorRejectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 异步更新的公用类
 * @author qiunet
 *         Created on 17/2/11 08:04.
 */
public class AsyncJobSupport {
	private Logger logger = LoggerFactory.getLogger(LoggerType.QIUNET_DATAS);
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
		Iterator<AsyncNode> it = nodes.iterator();
		while(it.hasNext()){
			final AsyncNode node = it.next();
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// 必须try catch 否则导致线程停止
						node.updateRedisDataToDatabase();
					}catch (Exception e) {
						logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
					}
				}
			});
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
			}
		}
	}

}
