package org.qiunet.function.ai.log;

import com.google.common.collect.Lists;
import org.qiunet.utils.string.StringUtil;

import java.util.List;

/***
 * 行为树每次tick的日志
 *
 * @author qiunet
 * 2022/7/28 14:56
 */
public class BehaviorTreeLogger {
	/**
	 * 日志 list
	 */
	private final List<String> list = Lists.newLinkedList();

	/**
	 * 拼加日志
	 * @param format
	 * @param params
	 */
	public void append(String format, Object... params) {
		this.list.add(StringUtil.slf4jFormat(format, params));
	}

	/**
	 * 打印日志
	 */
	public void print() {
		
	}

	/**
	 * 清理日志
	 */
	public void cleanup() {
		this.list.clear();
	}
}
