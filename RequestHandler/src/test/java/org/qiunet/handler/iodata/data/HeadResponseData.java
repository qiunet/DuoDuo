package org.qiunet.handler.iodata.data;

import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.net.IoData;

/**
 * @author qiunet
 *         Created on 17/3/10 17:46.
 */
public class HeadResponseData implements IoData{
	private long dt;

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}

	@Override
	public void dataReader(InputByteStream in) throws Exception {
		this.dt = in.readLong("dt");
	}

	@Override
	public void dataWriter(OutputByteStream out) throws Exception {
		if (dt == 0) dt = System.currentTimeMillis();
		out.writeLong("dt" , dt);
	}
}
