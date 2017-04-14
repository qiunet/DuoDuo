package org.qiunet.handler.iodata.adapter;

import org.qiunet.handler.iodata.base.InputByteDataStream;
import org.qiunet.handler.iodata.base.InputByteStream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * @author qiunet
 *         Created on 17/3/1 20:15.
 */
public class InputByteStreamBuilder {
	public static InputByteStream getInputByteStream(byte [] bytes) {
		return new InputByteDataStream(new ByteDataInputStream(bytes));
	}

	private static class ByteDataInputStream implements InputByteStream{
		private ByteArrayInputStream bis;
		private DataInputStream dis;
		
		private ByteDataInputStream(byte [] bytes) {
			this.bis = new ByteArrayInputStream(bytes);
			this.dis = new DataInputStream(bis);
		}
		@Override
		public byte readByte(String desc) throws Exception {
			return dis.readByte();
		}

		@Override
		public short readShort(String desc) throws Exception {
			return dis.readShort();
		}

		@Override
		public int readInt(String desc) throws Exception {
			return dis.readInt();
		}

		@Override
		public long readLong(String desc) throws Exception {
			return dis.readLong();
		}

		@Override
		public float readFloat(String desc) throws Exception {
			return dis.readFloat();
		}

		@Override
		public double readDouble(String desc) throws Exception {
			return dis.readDouble();
		}

		@Override
		public boolean readBoolean(String desc) throws Exception {
			return dis.readBoolean();
		}

		@Override
		public String readString(String desc)throws Exception {
			return dis.readUTF();
		}

		@Override
		public void close() throws Exception {
			dis.close();
			bis.close();
		}

		@Override
		public byte[] readBytes(int length) throws Exception {
			byte [] bytes = new byte[length];
			dis.read(bytes);
			return bytes;
		}
	}
}
