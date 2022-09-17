package org.qiunet.test.cross.common.data;

import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.utils.fakeenum.FakeEnumClass;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
@FakeEnumClass
public class PlayerCrossData {

	public static final CrossData<TestCrossDataUser> TEST_CROSS_DATA = new CrossData<TestCrossDataUser>() {
		@Override
		public TestCrossDataUser get(PlayerActor playerActor) {
			return TestCrossDataUser.valueOf("Qiunet", playerActor.getId());
		}

		@Override
		public void update(PlayerActor playerActor, TestCrossDataUser data) {
			LoggerType.DUODUO_FLASH_HANDLER.info("Update cross data: {}", data.getPlayerName());
		}
	};
}
