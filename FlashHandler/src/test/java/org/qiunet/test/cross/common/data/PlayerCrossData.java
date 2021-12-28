package org.qiunet.test.cross.common.data;

import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.flash.handler.common.player.PlayerActor;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
public class PlayerCrossData {

	public static final CrossData<TestCrossDataUser> TEST_CROSS_DATA = new CrossData<TestCrossDataUser>("TEST_CROSS_DATA") {
		@Override
		public TestCrossDataUser create(PlayerActor playerActor) {
			return TestCrossDataUser.valueOf("Qiunet", playerActor.getId());
		}
	};
}
