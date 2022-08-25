package org.qiunet.game.test.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.test.robot.creator.IRobotAccountFactory;
import org.qiunet.game.test.robot.creator.PressureConfig;
import org.qiunet.utils.scanner.anno.AutoWired;
import org.qiunet.utils.timer.IScheduledTask;
import org.qiunet.utils.timer.TimerManager;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/***
 * 机器人创建
 *
 * @author qiunet
 * 2022/8/25 11:42
 */
 enum RobotManager {
	instance;
	/**
	 * 所有机器人
	 */
	private final List<Robot> robots = Lists.newLinkedList();

	@AutoWired
	private IRobotAccountFactory robotAccountFactory;
	/**
	 * 停止
	 * @return
	 */
	public void stop() {
		robots.forEach(Robot::destroy);
	}

	/**
	 * 创建一个机器人
	 */
	synchronized void create() {
		Robot robot = new Robot(robotAccountFactory.newAccount(), PressureConfig.getTick(), PressureConfig.isLogPolicy());
		this.robots.add(robot);
	}
	/**
	 * 创建一定数量的机器人
	 * @param count 数量
	 * @return 机器人的列表
	 */
	void create(int count) {
		Preconditions.checkState(count > 0);
		if (count == 1) {
			create();
			return;
		}
		CountDownLatch latch = new CountDownLatch(count);
		IScheduledTask create = () -> {
			create();
			latch.countDown();

		};
		// 先登录一个 . 服务器的各种延迟加载的数据加载好.
		create.run();
		// 要错开登录. 不要一起
		ScheduledFuture<?> scheduledFuture = TimerManager.instance.scheduleAtFixedRate(create, 5000, PressureConfig.getInterval(), TimeUnit.MILLISECONDS);
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}finally {
			scheduledFuture.cancel(false);
		}
	}
}
