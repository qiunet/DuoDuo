package org.qiunet.cross.test.data;

import org.qiunet.cross.actor.data.BaseTransferData;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
public class TestCrossData extends BaseTransferData {

	private String playerName;

	private long uid;

	public static TestCrossData valueOf(String playerName, long uid) {
		TestCrossData crossData = new TestCrossData();
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
