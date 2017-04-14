package org.qiunet.data.enums;


import org.qiunet.utils.exceptions.EnumParseException;

/**
 * 目前手机游戏平台
 * @author qiunet
 *         Created on 16/12/29 08:24.
 */
public enum PlatformType {
	/**
	 * ios平台
	 */
	IOS("i"),
	/**
	 * 安卓平台
	 */
	ANDROID("a");
	
	private String simpleName;
	private PlatformType(String simpleName) {
		this.simpleName = simpleName;
	}
	
	/**
	 * 返回简称 a  i
	 * @return name
	 */
	public String getName(){
		return simpleName;
	}
	
	public static PlatformType parse(int val){
		if (val < 0 || val >= values().length) throw new EnumParseException(val);
		return values()[val];
	}
	
	public static PlatformType parse(String a) {
		for (PlatformType t : values()) {
			if (t.simpleName.equals(a)) return t;
		}
		throw new EnumParseException(a);
	}
}
