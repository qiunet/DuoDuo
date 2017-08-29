package org.qiunet.utils.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户的锁管理对象
 * Created by qiunet.
 * 17/8/29
 */
public class UserLockManager<UserID> implements Runnable {
	// 最大等待锁的线程数
	public static final int MAX_THREAD_COUNT_HOLD_LOCK = 3;
	// 检查userlock超时的间隔时间
	private static final int CHECK_USER_LOCK_GAP_DT = 5 * 60 * 1000;
	// 用户的锁,如果多久不活跃, 就会剔除
	private static final int USER_LOCK_TIMEOUT_DT = 2 * 60 * 60 * 1000;
	// 用户的锁管理
	private ConcurrentHashMap<UserID, UserLock> locks;

	private Thread thread;

	public UserLockManager() {
		locks = new ConcurrentHashMap<>();
		thread = new Thread(this, "UserLockManager");
		thread.setDaemon(true);
		thread.start();
	}

	/***
	 * 得到用户锁, 如果已经有几条thread等待. 返回false .告诉客户端请求频繁.
	 * 如果当前有处理的, 则放入linkedlist 等待
	 * @param id 用户的id
	 * @return
	 */
	public boolean lock(UserID id) {
		UserLock lock = locks.get(id);
		if (lock == null) {
			locks.putIfAbsent(id, createUserLock(id));
			lock = locks.get(id);
		}
		return lock.lock();
	}

	/***
	 * 创建一个用户锁. 这里需要同步
	 * @param id
	 * @return
	 */
	private synchronized UserLock createUserLock(UserID id) {
		return new UserLock(id);
	}

	/**
	 * 释放锁
	 * @param id
	 */
	public void releaseLock(UserID id) {
		UserLock lock = locks.get(id);
		if (lock != null) {
			lock.releaseLock();
		}
	}

	/****
	 * 用户的锁对象
	 * @param <UserID>
	 */
	private class UserLock<UserID> {
		private UserID id;
		private long dt;
		private AtomicInteger lockedCount;
		private ReentrantLock lock;

		UserLock(UserID id) {
			this.id = id;
			this.lock = new ReentrantLock();
			this.lockedCount = new AtomicInteger();
			this.dt = System.currentTimeMillis();
		}

		public boolean lock(){
			if (this.tryLock()) {
				lock.lock();
				return true;
			}
			return false;
		}

		private synchronized boolean tryLock(){
			boolean canLock = lockedCount.get() < MAX_THREAD_COUNT_HOLD_LOCK;
			if (canLock) {
				lockedCount.incrementAndGet();
			}
			return canLock;
		}

		public void releaseLock() {
			if (lock.isHeldByCurrentThread()){
				this.lock.unlock();
				this.lockedCount.decrementAndGet();
			}
		}
	}
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(CHECK_USER_LOCK_GAP_DT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long now = System.currentTimeMillis();
			for (UserLock lock : locks.values()) {
				if (now - lock.dt > USER_LOCK_TIMEOUT_DT) {
					locks.remove(lock.id);
				}
			}
		}
	}
}
