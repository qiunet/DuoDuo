package org.qiunet.flash.handler.context.session;
/**
 * 用户的session
 * Created by qiunet.
 * 17/10/23
 */
public interface IPlayerSession extends ISession {
	/***
	 * 得到用户的uid
	 * @return
	 */
	int getUid();

	/***
	 * 有uid了设置uid
	 * @param uid
	 */
	void setUid(int uid);
}
