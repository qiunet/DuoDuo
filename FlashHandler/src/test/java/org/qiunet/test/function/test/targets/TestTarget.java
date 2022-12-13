package org.qiunet.test.function.test.targets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.function.targets.TargetContainer;
import org.qiunet.function.targets.Targets;
import org.qiunet.test.function.test.TestDSession;
import org.qiunet.test.function.test.targets.event.KillBossEvent;
import org.qiunet.test.function.test.targets.event.LevelUpEvent;
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
	private final Logger logger = LoggerType.DUODUO.getLogger();
	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.TARGET_HANDLER).scanner();
	}

	@Test
	public void test(){
		var targetDefGetter = new TargetDefList<>(TargetDef.valueOf(1, TargetType.LEVEL, 10),
				TargetDef.valueOf(2, TargetType.KILL_BOSS, 2, "111"));

		PlayerActor playerActor = new PlayerActor(new TestDSession());
		playerActor.setMsgExecuteIndex("Test");

		TargetContainer<TargetType> targetContainer = TargetContainer.get(playerActor);
		Targets targets = targetContainer.createAndWatchTargets(targetDefGetter,
			(targets0, target) -> {
				logger.info("任务组ID:[{}],tid:[{}] 有更新, 当前值:[{}], 目标是否完成:[{}]!", targets0.getId(),target.getTid(), target.getValue(), target.isFinished());
				if (targets0.isFinished()) {
					logger.info("任务ID:[{}]已经完成", targets0.getId());
				}
			}, 1);

		playerActor.fireEvent(LevelUpEvent.valueOf(5));
		playerActor.fireEvent(LevelUpEvent.valueOf(11));

		playerActor.fireEvent(KillBossEvent.valueOf(224));
		playerActor.fireEvent(KillBossEvent.valueOf(111));
		playerActor.fireEvent(KillBossEvent.valueOf(111));


		// 测试从数据库加载json后. 任务的情况.
		var json = JsonUtil.toJsonString(targets);
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
