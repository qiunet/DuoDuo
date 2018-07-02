package org.qiunet.data.db.support.info.openidInfo;

import org.qiunet.data.db.support.info.base.string.StringEntityDbInfo;
/**
 * @author qiunet
 *         Created on 17/2/13 11:48.
 */
public class OpenidEntityDbInfo extends StringEntityDbInfo {
	private String openid;
	public OpenidEntityDbInfo(Object openid) {
		super(openid.toString());
		this.openid = openid.toString();
	}
	public String getOpenid() {
		return openid;
	}
}
