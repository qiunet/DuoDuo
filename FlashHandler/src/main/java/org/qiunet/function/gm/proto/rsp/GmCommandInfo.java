package org.qiunet.function.gm.proto.rsp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.function.gm.GmParam;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2021-01-09 10:41
 */
@ProtobufClass(description = "gm 命令信息")
public class GmCommandInfo {
	@Protobuf(description = "类型id")
	private int type;
	@Protobuf(description = "名称")
	private String name;
	@Protobuf(description = "参数")
	private List<GmParam> params;

	public static GmCommandInfo valueOf(int type, String name, List<GmParam> params) {
		GmCommandInfo info = new GmCommandInfo();
		info.type = type;
		info.name = name;
		info.params = params;
		return info;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GmParam> getParams() {
		return params;
	}

	public void setParams(List<GmParam> params) {
		this.params = params;
	}
}
