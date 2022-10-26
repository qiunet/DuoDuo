package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.util.proto.CommonModuleProto;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/2/9 11:14
 */
@ProtobufClass
public class RecursiveTest {
	@Protobuf
	private List<RecursiveTest> list;
	@Protobuf
	private String value;

	public List<RecursiveTest> getList() {
		return list;
	}

	public void setList(List<RecursiveTest> list) {
		this.list = list;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
