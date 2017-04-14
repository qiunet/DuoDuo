package org.qiunet.utils.pool;

import org.apache.log4j.Logger;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.exceptions.PoolException;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * 池的配置
 * 构造方法. propertyData 应该包含以下key
 * # simpleClassName.[maxIdel minIdel maxActiv]
 * # 代表 最大空闲  最小空闲  最大活跃
 * 
 * HttpClientPool.maxIdel=20
 * HttpClientPool.minIdel=10
 * HttpClientPool.maxActive=100
 * HttpClientPool.maxWaitTimeout=100
 * 
 * HttpsClientPool.maxIdel=20
 * HttpsClientPool.minIdel=10
 * HttpsClientPool.maxActive=100
 * HttpsClientPool.maxWaitTimeout=100
 * @param <T>
 */
public abstract class BasicPool<T> implements Pool<T>  {
	protected Logger logger = Logger.getLogger(getClass());
	/**锁*/
	private Lock lock;
	
	private String simpleClassName;
	/**最大空闲 多余的对象.不再回收.*/
	private int maxIdle=50;
	/**最小空闲 将创建 initSize - minIdel 个*/
	private int minIdle=10;
	/**如果 超出最大活跃, 则打印日志.*/
	private int maxActive=100;
	/**如果超时则返回null*/
	private int maxWaitTimeout=3000;

	/*空闲的. 需要在里面去一个 没有就创建*/
	private LinkedBlockingQueue<T> idles = new LinkedBlockingQueue();
	/*活跃的数量*/
	private AtomicInteger activeCount = new AtomicInteger();

	public BasicPool (IKeyValueData keyValueData){
		this.lock = new ReentrantLock();
		
		this.simpleClassName = getClass().getSimpleName();
		
		this.maxIdle = keyValueData.getInt(getKey("maxIdle"), 50);
		this.minIdle = keyValueData.getInt(getKey("minIdle"), 10);
		this.maxActive = keyValueData.getInt(getKey("maxActive"), 100);
		this.maxWaitTimeout = keyValueData.getInt(getKey("maxWaitTimeout"), 3000);
	}
	/**
	 * 创建T
	 * @return
	 */
	protected abstract T create();
	/***
	 * 有需要释放的操作.
	 * @param t
	 */
	protected abstract void clear(T t);
	/**
	 * 关闭对象.
	 * @param t
	 */
	protected abstract void close(T t);

	/**
	 * 从 池 取一个对象
	 */
	@Override
	public T getFromPool(){
		T t = null;
		if(idles.size() < minIdle && (idles.size() + activeCount.get()) < maxActive ){
			t = this.create();
		}
		
		if (t == null){
			try {
				t = idles.poll(maxWaitTimeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				logger.error("getFromPool wait timeout ");
				e.printStackTrace();
			}
		}
		
		if (t == null) {
			throw new PoolException("ActiveCount ["+getActiveCount()+"] is already max ");
		}else {
			activeCount.incrementAndGet();
		}
		return t;
	}
	/***
	 * 放回 池 里面
	 */
	@Override
	public void recycle(T  t){
		if (t == null) return;
		try {
			lock.lock();
			activeCount.decrementAndGet();
			if(idles.size() < maxIdle){
				// 只有小于最大空闲. 才放回池.
				this.clear(t);
				idles.add(t);
			}else {
				close(t);
			}
		}finally {
			lock.unlock();
		}
	}

	/***
	 * 得到对应的key
	 * @param subKey  子key
	 * @return  返回真实的key
	 */
	private String getKey(String subKey){
		return simpleClassName+"."+subKey;
	}
	@Override
	public int getMaxIdle() {
		return maxIdle;
	}
	@Override
	public int getMinIdle() {
		return minIdle;
	}
	@Override
	public int getMaxWaitTimeout() {
		return maxWaitTimeout;
	}
	@Override
	public int getMaxActive() {
		return maxActive;
	}
	@Override
	public int getIdelCount() {
		return idles.size();
	}
	@Override
	public int getActiveCount() {
		return activeCount.get();
	}
	@Override
	public String toString() {
		return "IdleCount["+getIdelCount()+"] ActiveCount["+getActiveCount()+"]";
	}
}
