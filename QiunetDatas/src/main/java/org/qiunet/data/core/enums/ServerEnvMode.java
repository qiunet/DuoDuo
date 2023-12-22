package org.qiunet.data.core.enums;

/***
 * 环境模式
 * @author qiunet
 * 2022/6/24 11:57
 */
public enum ServerEnvMode {
	/**
	 * 本地单元测试 很多可以不执行
	 */
	UNIT_TEST,
	/**
	 * debug模式 所有协议都打印
	 */
	DEBUG,
	/**
	 * 本地开发
	 */
	LOCAL,
	/**
	 * 开发测试环境
	 */
	DEV,
	/**
	 * 稳定测试环境
	 * 基本跟线上保持一直.
	 * 可以使用GM命令
	 */
	STABLE,
	/**
	 * 预发布环境.
	 * 或者提审环境
	 */
	PREVIEW,
	/**
	 * 正式环境
	 */
	OFFICIAL
}
