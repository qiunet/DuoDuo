package org.qiunet.utils.async.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/***
 *
 * @author qiunet
 * 2020/3/8 22:01
 **/
public class DCompletePromise<V> extends CompletableFuture<V> implements DFuture<V>, DPromise<V>, CompletionStage<V> {
	/**
	 * Threading - synchronized(this). We are required to hold the monitor to use Java's underlying wait()/notifyAll().
	 */
	private short waiters;
	/**
	 * 保证cancel时候, 能cancel原有的任务.
	 */
	private Future<V> future;

	public DCompletePromise() {
	}

	DCompletePromise(Future<V> future) {
		this.future = future;
	}

	public void setFuture(Future<V> future) {
		this.future = future;
	}

	@Override
	public boolean isCancelled() {
		if (future != null) {
			return future.isCancelled();
		}
		return super.isCancelled();
	}


	@Override
	public boolean isSuccess() {
		return isDone() && ! isCompletedExceptionally();
	}

	@Override
	public boolean trySuccess(V result) {
		return complete(result);
	}

	@Override
	public boolean tryFailure(Throwable cause) {
		boolean triggered = completeExceptionally(cause);
		if (triggered) {
			this.checkNotifyWaiters();
		}
		return triggered;
	}

	@Override
	public boolean complete(V value) {
		boolean triggered = super.complete(value);
		if (triggered) {
			this.checkNotifyWaiters();
		}
		return triggered;
	}

	private void incWaiters() {
		if (this.waiters == Short.MAX_VALUE) {
			throw new IllegalStateException("too many waiters: " + this);
		} else {
			++this.waiters;
		}
	}

	private void decWaiters() {
		--waiters;
	}

	@Override
	public DPromise<V> await() throws InterruptedException {
		if (this.isDone()) {
			return this;
		} else if (Thread.interrupted()) {
			throw new InterruptedException(this.toString());
		} else {
			synchronized(this) {
				while(!this.isDone()) {
					this.incWaiters();

					try {
						this.wait();
					} finally {
						this.decWaiters();
					}
				}

				return this;
			}
		}
	}

	@Override
	public DPromise<V> awaitUninterruptibly() {
		if (isDone()) {
			return this;
		}

		boolean interrupted = false;
		synchronized (this) {
			while (!isDone()) {
				incWaiters();
				try {
					wait();
				} catch (InterruptedException e) {
					// Interrupted while waiting.
					interrupted = true;
				} finally {
					decWaiters();
				}
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		return this;
	}

	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return await0(unit.toNanos(timeout), true);
	}

	@Override
	public boolean await(long timeoutMillis) throws InterruptedException {
		return await0(MILLISECONDS.toNanos(timeoutMillis), true);
	}

	@Override
	public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
		try {
			return await0(unit.toNanos(timeout), false);
		} catch (InterruptedException e) {
			// Should not be raised at all.
			throw new InternalError();
		}
	}

	@Override
	public boolean awaitUninterruptibly(long timeoutMillis) {
		try {
			return await0(MILLISECONDS.toNanos(timeoutMillis), false);
		} catch (InterruptedException e) {
			// Should not be raised at all.
			throw new InternalError();
		}
	}

	private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
		if (isDone()) {
			return true;
		}

		if (timeoutNanos <= 0) {
			return isDone();
		}

		if (interruptable && Thread.interrupted()) {
			throw new InterruptedException(toString());
		}

		long startTime = System.nanoTime();
		long waitTime = timeoutNanos;
		boolean interrupted = false;
		try {
			for (;;) {
				synchronized (this) {
					if (isDone()) {
						return true;
					}
					incWaiters();
					try {
						wait(waitTime / 1000000, (int) (waitTime % 1000000));
					} catch (InterruptedException e) {
						if (interruptable) {
							throw e;
						} else {
							interrupted = true;
						}
					} finally {
						decWaiters();
					}
				}
				if (isDone()) {
					return true;
				} else {
					waitTime = timeoutNanos - (System.nanoTime() - startTime);
					if (waitTime <= 0) {
						return isDone();
					}
				}
			}
		} finally {
			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	/**
	 * Check if there are any waiters and if so notify these.
	 * @return {@code true} if there are any listeners attached to the promise, {@code false} otherwise.
	 */
	private synchronized void checkNotifyWaiters() {
		if (waiters > 0) {
			notifyAll();
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (future != null) {
			future.cancel(mayInterruptIfRunning);
		}

		boolean ret = super.cancel(mayInterruptIfRunning);
		if (ret) {
			this.checkNotifyWaiters();
		}
		return ret;
	}
}
