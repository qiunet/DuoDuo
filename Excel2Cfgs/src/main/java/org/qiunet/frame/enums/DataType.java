package org.qiunet.frame.enums;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 数据类型
 * Created by qiunet.
 * 17/10/30
 */
public enum DataType {
	/**
	 * string
	 */
	DATA_STRING("string") {
		@Override
		public void writeData(DataOutputStream dos, String val) throws IOException {
			dos.writeUTF(val);
		}
	},
	/**
	 * double
	 */
	DATA_DOUBLE("double") {
		@Override
		public void writeData(DataOutputStream dos, String val) throws IOException {
			dos.writeDouble(Double.parseDouble(val));
		}
	},
	/***
	 * int
	 *
	 */
	DATA_INT("int") {
		@Override
		public void writeData(DataOutputStream dos, String val) throws IOException {
			dos.writeInt(Integer.parseInt(val));
		}
	},
	/***
	 * long 类型
	 */
	DATA_LONG("long") {
		@Override
		public void writeData(DataOutputStream dos, String val) throws IOException {
			dos.writeLong(Long.parseLong(val));
		}
	},
	;

	private String type;
	private DataType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static DataType parse(String val){
		for(DataType t : values()) {
			if(t.getType().equals(val.toLowerCase())) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 写入数据到dataOutputStream
	 * @param dos
	 * @param val
	 * @throws IOException
	 */
	public abstract void writeData(DataOutputStream dos, String val)throws IOException;
}
