package org.qiunet.game.tests.protocol.enums;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 * 性别类型
 *
 * qiunet
 * 2021/8/1 21:43
 **/
@ProtobufClass(description = "性别类型")
public enum GenderType implements EnumReadable {
	@Protobuf(description = "秘密")
	SECRET,
	@Protobuf(description = "男性")
	MALE,
	@Protobuf(description = "女性")
	FEMALE,
	;

	@Override
	public int value() {
		return ordinal();
	}
}
