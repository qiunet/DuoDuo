package org.qiunet.flash.handler.context.session;
/**
 * 用户的session
 * Created by qiunet.
 * 17/10/23
 */
public interface IPlayerSession<Key> extends ISession<Key> {
	/***
	 * 得到用户的uid
	 * @return
	 */
	int getUid();
}
