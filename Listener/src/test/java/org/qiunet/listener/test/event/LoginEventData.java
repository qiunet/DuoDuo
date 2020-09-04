package org.qiunet.listener.test.event;

import org.qiunet.listener.event.IEventData;

public class LoginEventData implements IEventData {

	private long uid;

	public LoginEventData(long uid) {
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

}
