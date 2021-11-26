package org.qiunet.cross.test.common.data;

import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
public class PlayerCrossData {

	public static final CrossData<TestCrossDataCross> TEST_CROSS_DATA = new CrossData<TestCrossDataCross>("TEST_CROSS_DATA") {
		@Override
		public TestCrossDataCross create(AbstractPlayerActor playerActor) {
			return TestCrossDataCross.valueOf("Qiunet", playerActor.getId());
		}
	};
}
