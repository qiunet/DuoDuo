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
public class Recursive2 {
	@Protobuf
	private List<Recursive1> list;
	@Protobuf
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
