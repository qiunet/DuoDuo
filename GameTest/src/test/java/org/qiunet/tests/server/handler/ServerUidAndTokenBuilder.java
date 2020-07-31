package org.qiunet.tests.server.handler;

/**
 * 有个规则就行. 主要是验证
 * Created by qiunet.
 * 17/12/9
 */
public class ServerUidAndTokenBuilder {
	public static final String OPENID_PREFIX = "Qiunet_";
	/***
	 *
	 * @param openid
	 * @return
	 */
	public static int getUid(String openid) {
		return Integer.parseInt(openid.substring(OPENID_PREFIX.length()));
	}

	public static String getToken(String openid){
		return openid;
	}
}
