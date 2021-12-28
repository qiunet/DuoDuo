package org.qiunet.test.cross.common.data;

import org.qiunet.cross.actor.data.IUserTransferData;

/***
 *
 *
 * @author qiunet
 * 2020-10-28 12:19
 */
public class TestCrossDataUser implements IUserTransferData {

	private String playerName;

	private long uid;

	public static TestCrossDataUser valueOf(String playerName, long uid) {
		TestCrossDataUser crossData = new TestCrossDataUser();
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
