package org.qiunet.function.gm;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.utils.exceptions.CustomException;

/***
 *
 *
 * @author qiunet
 * 2021-01-08 10:57
 */
@ProtobufClass(description = "gm 参数类型枚举")
public enum GmParamType {
	@Protobuf(description = "int 类型")
	INT,
	@Protobuf(description = "long 类型")
	LONG,
	@Protobuf(description = "string 类型")
	STRING,
	;
	public static GmParamType parse(Class<?> clz) {
		if (clz == Integer.class || clz == Integer.TYPE) {
			return INT;
		}

		if (clz == Long.class || clz == Long.TYPE) {
			return LONG;
		}

		if (clz == String.class) {
			return STRING;
		}

		throw new CustomException("Not support type for {} ", clz.getName());
	}
}
