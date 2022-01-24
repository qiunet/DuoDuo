package org.qiunet.function.condition.doc;

/***
 *
 * @author qiunet
 * 2022/1/24 16:14
 */
public class ConditionParamDoc {

	private String name;

	private String type;

	private String desc;

	public static ConditionParamDoc valueOf(String name, Class<?> type, String desc) {
		ConditionParamDoc param = new ConditionParamDoc();
		param.type = paramType(type);
		param.name = name;
		param.desc = desc;
		return param;
	}

	private static String paramType(Class<?> clz) {
		if (clz == Integer.class || clz == int.class) {
			return "int";
		}
		if (clz == Long.class || clz == long.class) {
			return "long";
		}
		return "string";
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
}
