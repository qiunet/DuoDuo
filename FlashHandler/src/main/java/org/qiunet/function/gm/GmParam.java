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

	public static GmParam valueOf(GmParamType type, String name) {
		GmParam gmParam = new GmParam();
		gmParam.type = type;
		gmParam.name = name;
		return gmParam;
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
