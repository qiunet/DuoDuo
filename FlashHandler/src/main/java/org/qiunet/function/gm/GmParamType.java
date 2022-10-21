package org.qiunet.function.gm;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.exceptions.CustomException;

/***
 * gm 参数类型枚举
 *
 * @author qiunet
 * 2021-01-08 10:57
 */
@ProtobufClass(description = "gm 参数类型枚举")
public enum GmParamType {
	@Protobuf(description = "int 类型")
	INT(Integer.class, Integer.TYPE) {
		@Override
		public Object parse(String val) {
			return Integer.parseInt(val);
		}
	},
	@Protobuf(description = "long 类型")
	LONG(Long.class, Long.TYPE) {
		@Override
		public Object parse(String val) {
			return Long.parseLong(val);
		}
	},
	@Protobuf(description = "string 类型")
	STRING(String.class) {
		@Override
		public Object parse(String val) {
			return val;
		}
	},
	;

	private final Class<?> [] classes;

	GmParamType(Class<?>... classes) {
		this.classes = classes;
	}

	public abstract Object parse(String val);

	public static GmParamType parse(Class<?> clz) {
		for (GmParamType value : values()) {
			if (CommonUtil.existInList(clz, value.classes)) {
				return value;
			}
		}
		throw new CustomException("Not support type for {} ", clz.getName());
	}
}
