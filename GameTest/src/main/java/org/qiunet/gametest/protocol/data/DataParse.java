package org.qiunet.gametest.protocol.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 数据处理
 * @author qiunet
 *         Created on 16/12/16 11:47.
 */
public interface DataParse<IN, OUT> {
	/**
	 * 最好能打印值出来. json  StringData 打印都行
	 * @return
	 */
	abstract String toString();
	/**
	 * 解析进来的数据
	 * @param datain
	 */
	void parseDataIn(IN datain);

	/**
	 * 往外输出数据
	 * @param dataout
	 */
	void parseDataOut(OUT dataout);
}
