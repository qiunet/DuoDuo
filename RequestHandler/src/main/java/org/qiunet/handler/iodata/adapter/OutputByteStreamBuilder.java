package org.qiunet.handler.iodata.adapter;

import org.qiunet.handler.iodata.base.OutputByteDataStream;
import org.qiunet.handler.iodata.base.OutputByteStream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * @author qiunet
 *         Created on 17/3/1 20:37.
 */
public class OutputByteStreamBuilder {
	
	public static OutputByteStream getOutputByteStream() {
		return new OutputByteDataStream(new ByteDataOutputStream());
	}
	
	private static class ByteDataOutputStream implements OutputByteStream {
		private ByteArrayOutputStream baos;
		private DataOutputStream dos;
		private ByteDataOutputStream () {
			this.baos = new ByteArrayOutputStream();
			this.dos = new DataOutputStream(baos);
		}
		@Override
		public void writeByte(String desc, byte val) throws Exception {
			dos.writeByte(val);
		}

		@Override
		public void writeShort(String desc, short val) throws Exception {
			dos.writeShort(val);
		}

		@Override
		public void writeInt(String desc, int val) throws Exception {
			dos.writeInt(val);
		}

		@Override
		public void writeLong(String desc, long val) throws Exception {
			dos.writeLong(val);
		}

		@Override
		public void writeFloat(String desc, float val) throws Exception {
			dos.writeFloat(val);
		}

		@Override
		public void writeDouble(String desc, double val) throws Exception {
			dos.writeDouble(val);
		}

		@Override
		public void writeBoolean(String desc, boolean bool) throws Exception {
			dos.writeBoolean(bool);
		}

		@Override
		public void writeString(String desc, String string) throws Exception {
			dos.writeUTF(string);
		}

		@Override
		public void writeBytes(byte[] bytes) throws Exception {
			dos.write(bytes);
		}

		@Override
		public byte[] getBytes() throws Exception {
			return baos.toByteArray();
		}

		@Override
		public void close() throws Exception{
			dos.close();
			baos.close();
		}
	}
}
