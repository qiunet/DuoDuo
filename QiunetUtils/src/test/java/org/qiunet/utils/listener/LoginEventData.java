package org.qiunet.utils.listener;

@EventListener(LoginEventData.LoginListener.class)
public class LoginEventData implements IEventData {

	private long uid;

	public LoginEventData(long uid) {
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}


	public interface LoginListener {
		void onLogin(LoginEventData data);
	}
}
