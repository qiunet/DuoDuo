package org.qiunet.utils.test.listener;

import org.qiunet.utils.listener.event.IEventData;

public class LoginEventData implements IEventData {

	private final long uid;

	public LoginEventData(long uid) {
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

}
