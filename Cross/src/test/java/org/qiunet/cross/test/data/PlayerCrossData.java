package org.qiunet.cross.test.data;

import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
public class PlayerCrossData {

	public static final CrossData<TestCrossData> TEST_CROSS_DATA = new CrossData<TestCrossData>("TEST_CROSS_DATA") {
		@Override
		public TestCrossData create(AbstractPlayerActor playerActor) {
			return TestCrossData.valueOf("Qiunet", playerActor.getId());
		}
	};

	public static void main(String[] args) {
		PlayerCrossData crossData = new PlayerCrossData();
	}
}
