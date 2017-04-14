package org.qiunet.handler.iodata.net;

import org.qiunet.handler.exception.CrcErrorException;
import org.qiunet.handler.iodata.adapter.InputByteStreamBuilder;
import org.qiunet.handler.iodata.adapter.OutputByteStreamBuilder;
import org.qiunet.handler.iodata.base.InputByteStream;
import org.qiunet.handler.iodata.base.OutputByteStream;
import org.qiunet.handler.iodata.util.ProtocolUtil;
import org.qiunet.utils.data.StringData;
import org.qiunet.utils.math.MathUtil;

import java.util.zip.CRC32;

/**
 *  作为responsedata 和 requestData的父类. 设置通用的方法. 但是不对外开放. 仅本目录可见
 * @author qiunet
 *         Created on 17/3/3 14:53.
 */
abstract class BaseIoData<HEADER extends IoData, COMMON extends IoData>  implements IoData {
	private LeaderIoData leader;
	private HEADER header;
	private COMMON common;
	BaseIoData(HEADER header, COMMON common){
		leader = new LeaderIoData();
		this.header = header;
		this.common = common;
	}
	@Override
	public void dataReader(InputByteStream in) throws Exception {
		leader.dataReader(in);
		if (leader.getSplit() <= 0) leader.setSplit((short) 1);
		byte [] bytes = in.readBytes(leader.getLength());
		byte [] realBytes = ProtocolUtil.decryptData(bytes, leader.getSplit());
		CRC32 crc32 = new CRC32();
		crc32.update(realBytes);
		if (crc32.getValue() != leader.getCrc()) {
			throw new CrcErrorException("This request is crc error!");
		}
		
		InputByteStream body = InputByteStreamBuilder.getInputByteStream(realBytes);
		header.dataReader(body);
		common.dataReader(body);
		dataReaders(body);
		
		body.close();
	}

	@Override
	public void dataWriter(OutputByteStream out) throws Exception {
		OutputByteStream body = OutputByteStreamBuilder.getOutputByteStream();
		
		header.dataWriter(body);
		common.dataWriter(body);
		dataWriters(body);
		
		byte [] bodyBytes = body.getBytes();
		int split = MathUtil.random(bodyBytes.length/10, bodyBytes.length/3);
		if (split > Short.MAX_VALUE) split = Short.MAX_VALUE - 1;
		else if (split <= 0) split = 1;
		
		CRC32 crc32 = new CRC32();
		crc32.update(bodyBytes);
		
		
		byte [] splitBytes = ProtocolUtil.encryptData(bodyBytes, split);
		
		leader.setCrc(crc32.getValue());
		leader.setSplit((short) split);
		leader.setLength(bodyBytes.length);
		leader.dataWriter(out);
		out.writeBytes(splitBytes);
		
		body.close();
	}
	/**
	 * 子类的读取
	 * @param in
	 * @throws Exception
	 */
	protected abstract void dataReaders(InputByteStream in) throws Exception ;

	/**
	 *  子类的写入
	 * @param out
	 * @throws Exception
	 */
	protected abstract void dataWriters(OutputByteStream out) throws Exception ;
	/**
	 * 得到leader
	 * @return
	 */
	public LeaderIoData getLeader(){
		return leader;
	};
	/***
	 * 得到header
 	 * @return
	 */
	public HEADER getHeader(){
		return header;
	}
	/**
	 * 得到common
	 * @return
	 */
	public COMMON getCommon(){
		return common;
	}
	/**
	 *  toString
	 * @return
	 */
	@Override
	public String toString(){
		return StringData.parseString(this);
	}
}
