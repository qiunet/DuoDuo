package org.qiunet.game.test.robot.creator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.utils.timer.TimerManager;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/***
 *
 * robot 工厂接口
 *
 * @author qiunet
 * 2021-07-05 11:29
 */
public interface IRobotCreator {
	/**
	 * 创造一个机器人
	 * 创建时候. 把account 设置好.
	 * @return
	 */
	Robot create();

	/**
	 * 创建一定数量的机器人
	 * @param count 数量
	 * @return 机器人的列表
	 */
	default List<Robot> create(int count) {
		Preconditions.checkState(count > 0);
		if (count == 1) {
			return Lists.newArrayList(create());
		}
		List<Robot> list = Lists.newArrayListWithCapacity(count);
		CountDownLatch latch = new CountDownLatch(count);
		ScheduledFuture<?> scheduledFuture = TimerManager.instance.scheduleAtFixedRate(() -> {
			list.add(create());
			latch.countDown();
			// 要错开登录. 不要一起
		}, 0, 300, TimeUnit.MILLISECONDS);
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}finally {
			scheduledFuture.cancel(false);
		}
		return list;
	}
}
