package org.qiunet.function.condition.doc;

import org.qiunet.function.ai.node.action.BehaviorActionParam;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2022/2/23 11:45
 */
public class AiActionParam {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 正则表达式
	 */
	private String regex;
	/**
	 * 字符长度最小值.
	 * 或者数值的最小值.
	 */
	private long min;
	/**
	 * 字符长度最大值.
	 * 或者数值的最大值.
	 */
	private long max;

	public AiActionParam() {}

	public AiActionParam(Field field) {
		this.name = field.getName();
		this.type = field.getType().getSimpleName();
		BehaviorActionParam annotation = field.getAnnotation(BehaviorActionParam.class);
		this.desc = annotation.value();
		this.regex = annotation.regex();
		this.min = annotation.min();
		this.max = annotation.max();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}
}
