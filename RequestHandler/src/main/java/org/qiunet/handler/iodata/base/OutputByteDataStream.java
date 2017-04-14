package org.qiunet.handler.iodata.base;

import org.apache.log4j.Logger;
import org.qiunet.handler.log.ILogSwitch;

import java.util.Arrays;

/**
 * @author qiunet
 *         Created on 17/3/1 20:20.
 */
public class OutputByteDataStream implements OutputByteStream , ILogSwitch {
	private static final Logger logger = Logger.getLogger(InputByteDataStream.class);
	private boolean printLog;
	private OutputByteStream outByteStream;
	@Override
	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	public OutputByteDataStream(OutputByteStream outByteStream) {
		this.outByteStream = outByteStream;
		this.printLog = true;
	}
	
	@Override
	public void writeByte(String desc, byte val) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeByte["+desc+"]: "+val);
		}
		outByteStream.writeByte(desc, val);
	}

	@Override
	public void writeShort(String desc, short val) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeShort["+desc+"]: "+val);
		}
		outByteStream.writeShort(desc, val);
	}

	@Override
	public void writeInt(String desc, int val)  throws Exception{
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeInt["+desc+"]: "+val);
		}
		outByteStream.writeInt(desc, val);
	}

	@Override
	public void writeLong(String desc, long val) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeLong["+desc+"]: "+val);
		}
		outByteStream.writeLong(desc, val);
	}

	@Override
	public void writeFloat(String desc, float val) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeFloat["+desc+"]: "+val);
		}
		outByteStream.writeFloat(desc, val);
	}

	@Override
	public void writeDouble(String desc, double val) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeDouble["+desc+"]: "+val);
		}
		outByteStream.writeDouble(desc, val);
	}

	@Override
	public void writeBoolean(String desc, boolean bool) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeBoolean["+desc+"]: "+bool);
		}
		outByteStream.writeBoolean(desc, bool);
	}

	@Override
	public void writeString(String desc, String string) throws Exception {
		if (logger.isInfoEnabled() && printLog) {
			logger.info("writeString["+desc+"]: "+string);
		}
		outByteStream.writeString(desc, string);
	}

	@Override
	public void writeBytes(byte[] bytes) throws Exception {
		if (logger.isDebugEnabled() && printLog) {
			logger.info("writeBytes: "+ Arrays.toString(bytes));
		}
		outByteStream.writeBytes(bytes);
	}

	@Override
	public byte[] getBytes() throws Exception {
		return outByteStream.getBytes();
	}

	@Override
	public void close() throws Exception{
		if (logger.isDebugEnabled() && printLog) logger.debug("calling close");
		outByteStream.close();
	}
}
