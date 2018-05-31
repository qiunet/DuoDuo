package org.qiunet.utils.lock;

import org.qiunet.utils.hook.ShutdownHookThread;

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
	private int maxLockedCount;
	// 检查userlock超时的间隔时间
	private int CHECK_USER_LOCK_GAP_DT = 5 * 60 * 1000;
	// 用户的锁,如果多久不活跃, 就会剔除
	private int USER_LOCK_TIMEOUT_DT = 2 * 60 * 60 * 1000;
	// 用户的锁管理
	private ConcurrentHashMap<UserID, UserLock> locks;

	private Thread thread;

	public UserLockManager(int maxLockedCount) {
		if (maxLockedCount < 1) throw new IllegalArgumentException("maxLockedCount can not be "+maxLockedCount);

		this.maxLockedCount = maxLockedCount;

		locks = new ConcurrentHashMap<>();

		ShutdownHookThread.getInstance().addShutdownHook(() -> running = false);

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
			locks.putIfAbsent(id, new UserLock(id));
			lock = locks.get(id);
		}
		return lock.lock();
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

	/***
	 * 得到当前玩家锁住的线程数
	 * @param id
	 * @return
	 */
	public int getLockedCount(UserID id){
		UserLock lock = locks.get(id);
		return lock == null ? 0 : lock.lockedCount.get();
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

		/**
		 * 尝试去获取锁. 当已经使用的数量 > 指定数时候, 返回false.
		 * @return
		 */
		private synchronized boolean tryLock(){
			boolean canLock = lockedCount.get() < maxLockedCount;
			if (canLock) {
				lockedCount.incrementAndGet();
			}
			return canLock;
		}

		/***
		 * 释放用户的锁, 并且减数
		 */
		public void releaseLock() {
			if (lock.isHeldByCurrentThread()){
				this.lock.unlock();
				this.lockedCount.decrementAndGet();
			}
		}
	}

	private boolean running = true;
	@Override
	public void run() {
		while (running) {
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
