package org.qiunet.handler.iodata.entitys;

import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.base.BaseRequestData;

/**
 * @author qiunet
 *         Created on 17/3/10 17:43.
 */
public class LoginRequestData extends BaseRequestData {
	private String openid;
	private String token;
	private String secret;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	protected void dataReaders(InputByteStream in) throws Exception {
		this.openid = in.readString("openid");
		this.token = in.readString("token");
		this.secret = in.readString("secret");
	}

	@Override
	protected void dataWriters(OutputByteStream out) throws Exception {
		out.writeString("openid", openid);
		out.writeString("token", token);
		out.writeString("secret", secret);
		
	}
}
