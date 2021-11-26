package org.qiunet.test.handler.proto;


import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 13:06
 */
@ProtobufClass(description = "性别枚举")
public enum  GenderType implements EnumReadable {
	@Protobuf(description = "男")
	MAN(1),

	@Protobuf(description = "女")
	FAMALE(2),
	;
	private int val;


	GenderType(int val) {
		this.val = val;
	}

	@Override
	public int value() {
		return val;
	}
}
