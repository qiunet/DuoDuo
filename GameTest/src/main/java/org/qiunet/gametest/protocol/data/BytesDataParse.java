package org.qiunet.gametest.protocol.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
/**
 * 使用字节流的处理类应该继承该接口
 * @author qiunet
 *         Created on 16/12/14 17:14.
 */
public interface BytesDataParse extends DataParse<DataInputStream , DataOutputStream> {

}
