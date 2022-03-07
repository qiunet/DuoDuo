package org.qiunet.function.gm;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 * gm 命令的参数
 *
 * @author qiunet
 * 2021-01-08 10:59
 */
@ProtobufClass(description = "gm 命令的参数")
public class GmParam {

	@Protobuf(description = "类型")
	private GmParamType type;
	@Protobuf(description = "名称")
	private String name;
	@Protobuf(description="正则表达式")
	private String regex;
	@Protobuf(description="示例")
	private String example;

	public static GmParam valueOf(GmParamType type, String name, String regex, String example) {
		GmParam gmParam = new GmParam();
		gmParam.example = example;
		gmParam.regex = regex;
		gmParam.type = type;
		gmParam.name = name;
		return gmParam;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public GmParamType getType() {
		return type;
	}

	public void setType(GmParamType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
