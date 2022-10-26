package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */
@ProtobufClass
public class Recursive1 {
	@Protobuf
	private List<Recursive2> list;
	@Protobuf
	private String value;

	public List<Recursive2> getList() {
		return list;
	}

	public void setList(List<Recursive2> list) {
		this.list = list;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
