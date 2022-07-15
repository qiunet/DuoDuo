package org.qiunet.test.function.test.ai;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.test.function.test.ai.enums.Enemy;
import org.qiunet.utils.logger.LoggerType;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 *
 * qiunet
 * 2021/8/17 10:17
 **/
public class Hero extends MessageHandler<Hero> {
	/**
	 * 看见的敌人
	 */
	private final EnumSet<Enemy> enemySet = EnumSet.noneOf(Enemy.class);
	/**
	 * 走路的状态.
	 * 一般是服务器响应驱动修改坐标点.
	 * action 仅仅判断是否走到地方即可
	 */
	private final AtomicBoolean runToTargetPoint = new AtomicBoolean();

	/**
	 * 是否看见某个敌人
	 * @param enemy
	 * @return
	 */
	public boolean isSee(Enemy enemy) {
		return enemySet.contains(enemy);
	}
	/**
	 * 让看见某个敌人
	 * @param enemies
	 */
	public void seeEnemy(Enemy ... enemies) {
		for (Enemy enemy : enemies) {
			LoggerType.DUODUO.info("Hero 看见 {}", enemy.getName());
		}
		enemySet.addAll(Arrays.asList(enemies));
	}
	/**
	 * 清理视野
	 */
	public void clearSeeEnemy() {
		LoggerType.DUODUO.info("Hero 走到了一片空地上.");
		enemySet.clear();
	}

	public void runToTargetPoint(){
		LoggerType.DUODUO.info("Hero 跑向目标...");
		runToTargetPoint.set(true);

	}

	public void runFinished(){
		LoggerType.DUODUO.info("Hero 跑步结束!");
		runToTargetPoint.set(false);
	}

	@Override
	public String msgExecuteIndex() {
		return "hero";
	}
}
