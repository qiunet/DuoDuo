package org.qiunet.test.robot.init;

/**
 * Created by qiunet.
 * 17/12/6
 */
public class DefaultRobotInfo implements IRobotInitInfo{

	private String openId;

	private String sid;

	public DefaultRobotInfo (String openId, String sid) {
		this.openId = openId;
		this.sid = sid;
	}

	@Override
	public String getOpenId() {
		return openId;
	}

	@Override
	public String getSid() {
		return sid;
	}
}
