package org.qiunet.excel2cfgs.common.enums;

/***
 *
 * @author qiunet
 * 2020-10-31 21:11
 **/
public enum DataType {
	/**
	 * int
	 */
	INT("int32"),
	/**
	 * 字符串
	 */
	STRING("string"),
	/**
	 * long
	 */
	LONG("int64"),
	;

	private final String protoType;
	DataType(String protoType) {
		this.protoType = protoType;
	}

	public String getProtoType() {
		return protoType;
	}

	public static DataType parse(String val){
		return DataType.valueOf(val.toUpperCase());
	}
}
