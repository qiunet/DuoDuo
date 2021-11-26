package org.qiunet.cross.test.data;

import org.qiunet.cross.actor.data.BaseCrossTransferData;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
public class TestCrossDataCross extends BaseCrossTransferData {

	private String playerName;

	private long uid;

	public static TestCrossDataCross valueOf(String playerName, long uid) {
		TestCrossDataCross crossData = new TestCrossDataCross();
		crossData.playerName = playerName;
		crossData.uid = uid;
		return crossData;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
}
