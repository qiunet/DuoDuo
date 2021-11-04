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
	/**
	 * int 数组
	 */
	INT_ARRAY("string"),
	/**
	 * long 数组
	 */
	LONG_ARRAY("string"),
	;

	private final String protoType;
	DataType(String protoType) {
		this.protoType = protoType;
	}

	public String getProtoType() {
		return protoType;
	}

	public static DataType parse(String val){
		if ("int[]".equalsIgnoreCase(val)) {
			return INT_ARRAY;
		}
		if ("long[]".equalsIgnoreCase(val)) {
			return LONG_ARRAY;
		}
		return DataType.valueOf(val.toUpperCase());
	}
}
