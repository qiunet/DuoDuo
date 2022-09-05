package org.qiunet.test.function.test.targets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.function.targets.*;
import org.qiunet.test.function.test.TestDSession;
import org.qiunet.test.function.test.targets.event.KillBossEventData;
import org.qiunet.test.function.test.targets.event.LevelUpEventData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;
import org.slf4j.Logger;

/***
 *
 * @author qiunet
 * 2020-11-23 21:51
 **/
public class TestTarget {
	private Logger logger = LoggerType.DUODUO.getLogger();
	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.TARGET_HANDLER).scanner();
	}

	@Test
	public void test(){
		TargetDefList targetDefGetter = new TargetDefList(TargetDef.valueOf(1, TargetType.LEVEL, 10),
				TargetDef.valueOf(2, TargetType.KILL_BOSS, 2, "111"));

		PlayerActor playerActor = new PlayerActor(new TestDSession());

		TargetContainer<TargetType> targetContainer = TargetContainer.get(playerActor);
		Targets targets = targetContainer.createAndWatchTargets(targetDefGetter,
			(targets0, target) -> {
				logger.info("任务组ID:[{}],tid:[{}] 有更新, 当前值:[{}], 目标是否完成:[{}]!", targets0.getId(),target.getTid(), target.getValue(), target.isFinished());
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

		Targets targetsObj = JsonUtil.getGeneralObj(json, Targets.class);
		targetContainer.addTargets(targetDefGetter, (targets0, target) -> {
			logger.info("反序列化后: 任务组ID:[{}],tid:[{}] 有更新, 当前值:[{}], 目标是否完成:[{}]!", targets0.getId(),target.getTid(), target.getValue(), target.isFinished());
			if (targets0.isFinished()) {
				logger.info("反序列化后:  任务组ID:[{}]已经完成", targets0.getId());
			}
		}, targetsObj);
	}

}
