package org.qiunet.test.function.test.targets;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.function.targets.ITargetDefGetter;
import org.qiunet.function.targets.TargetContainer;
import org.qiunet.function.targets.TargetDefList;
import org.qiunet.function.targets.Targets;
import org.qiunet.test.function.test.TestDSession;
import org.qiunet.test.function.test.targets.event.KillBossEventData;
import org.qiunet.test.function.test.targets.event.LevelUpEventData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.ClassScanner;
import org.slf4j.Logger;

/***
 *
 * @author qiunet
 * 2020-11-23 21:51
 **/
public class TestTarget {
	private Logger logger = LoggerType.DUODUO.getLogger();
	@BeforeClass
	public static void init(){
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void test(){
		ITargetDefGetter targetDefGetter = () ->
			new TargetDefList(TargetDef.valueOf(TargetType.LEVEL, 10),
				TargetDef.valueOf(TargetType.KILL_BOSS, 2, "111"));
		PlayerActor playerActor = new PlayerActor(new TestDSession());

		Targets targets = PlayerDataKey.targetContainer.computeIfAbsent(playerActor, () -> new TargetContainer<>(playerActor)).createAndWatchTargets(targetDefGetter,
			(targets0, target) -> {
				logger.info("任务ID:[{}],index:[{}] 有更新, 当前值:[{}], 目标是否完成:[{}]!", targets0.getId(),target.getIndex(), target.getValue(), target.isFinished());
				if (targets0.isFinished()) {
					logger.info("任务ID:[{}]已经完成", targets0.getId());
				}
			}, 1);

		playerActor.fireEvent(LevelUpEventData.valueOf(5));
		playerActor.fireEvent(LevelUpEventData.valueOf(11));

		playerActor.fireEvent(KillBossEventData.valueOf(224));
		playerActor.fireEvent(KillBossEventData.valueOf(111));
		playerActor.fireEvent(KillBossEventData.valueOf(111));


		// 测试从数据库加载json后. 任务的情况.
		String json = JsonUtil.toJsonString(targets);
		logger.info("Targets Json: {}", json);

		Targets targetsObj = JsonUtil.getGeneralObject(json, Targets.class);
		PlayerDataKey.targetContainer.get(playerActor).addTargets(targetDefGetter, (targets0, target) -> {
			logger.info("=任务ID:[{}],index:[{}] 有更新, 当前值:[{}], 目标是否完成:[{}]!", targets0.getId(),target.getIndex(), target.getValue(), target.isFinished());
			if (targets0.isFinished()) {
				logger.info("=任务ID:[{}]已经完成", targets0.getId());
			}
		}, targetsObj);
	}

}
