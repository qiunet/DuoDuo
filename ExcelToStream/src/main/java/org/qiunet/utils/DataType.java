package org.qiunet.utils;

/**
 * 数据类型
 * Created by qiunet.
 * 17/10/30
 */
public enum  DataType {

	DATA_STRING("string"),

	DATA_DOUBLE("double"),

	DATA_INT("int"),

	DATA_LONG("long"),
	;

	private String type;
	private DataType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static DataType parse(String val){
		for(DataType t : values())
			if(t.getType().equals(val.toLowerCase()))
				return t;

		return null;
	}
}
