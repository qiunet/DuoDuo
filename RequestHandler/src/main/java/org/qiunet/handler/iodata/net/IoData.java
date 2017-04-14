package org.qiunet.handler.iodata.net;

import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;

/**
 * 负责数据的出入.
 * @author qiunet
 *         Created on 17/3/2 14:53.
 */
public interface IoData {
	/**
	 * 负责数据读取
	 * @param in
	 * @throws Exception
	 */
	public void dataReader(InputByteStream in)throws Exception;

	/**
	 * 数据的写入
	 * @param out
	 * @throws Exception
	 */
	public void dataWriter(OutputByteStream out)throws Exception;
}
