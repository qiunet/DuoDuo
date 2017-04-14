package org.qiunet.handler.iodata.data;

import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.net.IoData;

/**
 * @author qiunet
 *         Created on 17/3/10 17:45.
 */
public class CommonRequestData implements IoData {
	private int uid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	@Override
	public void dataReader(InputByteStream in) throws Exception {
		this.uid = in.readInt("uid");
	}

	@Override
	public void dataWriter(OutputByteStream out) throws Exception {
		out.writeInt("uid" , uid);
	}
}
