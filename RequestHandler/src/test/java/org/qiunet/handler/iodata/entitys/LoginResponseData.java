package org.qiunet.handler.iodata.entitys;

import org.qiunet.handler.iodata.base.BaseResponseData;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;

/**
 * @author qiunet
 *         Created on 17/3/10 18:25.
 */
public class LoginResponseData extends BaseResponseData {
	private int uid;
	private String sid;
	public LoginResponseData () {
		super((short) 10);
	}
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	@Override
	protected void dataReaders(InputByteStream in) throws Exception {
		this.uid = in.readInt("uid");
		this.sid = in.readString("sid");
	}

	@Override
	protected void dataWriters(OutputByteStream out) throws Exception {
		out.writeInt("uid", uid);
		out.writeString("sid", sid);
	}
}
