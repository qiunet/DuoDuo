package org.qiunet.listener.test.observer;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.listener.observer.ObserverSupport;

/***
 *
 *
 * @author qiunet
 * 2020-09-12 12:46
 */
public class TestObserver {
	private static long PLAYER_ID = 10003424;
	private static ObserverSupport support = new ObserverSupport();
	@Test
	public void test(){
		support.attach(IPlayerMoveObserver.class, (playerId) -> {
			Assert.assertEquals(PLAYER_ID, playerId);
		});

		support.fire(IPlayerMoveObserver.class, iPlayerMoveObserver -> iPlayerMoveObserver.move(PLAYER_ID));
	}
}
