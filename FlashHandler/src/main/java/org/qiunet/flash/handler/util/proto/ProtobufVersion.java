package org.qiunet.flash.handler.util.proto;

import com.baidu.bjf.remoting.protobuf.utils.FieldInfo;

/***
 * protobuf 版本
 * @author qiunet
 * 2022/2/9 09:46
 */
public enum ProtobufVersion {

	V2 {
		/** The Constant V2_HEADER. */
		private static final String V2_HEADER = "syntax=\"proto2\";\n\n";

		@Override
		public StringBuilder fileContentStringBuffer(boolean needImportCommonProto) {
			StringBuilder sb = new StringBuilder(V2_HEADER);
			if (needImportCommonProto) {
				sb.append(IMPORT_COMMON_PROTO);
			}
			if (GeneratorProtoFeature.DEFAULT_PROTO_PACKAGE.prepare()) {
				sb.append(PACKAGE_MESSAGE);
			}
			return sb;
		}

		@Override
		public String getFieldDescribe() {
			return "\toptional ";
		}

		@Override
		public String getRepeatedFieldDescribe(FieldInfo field) {
			Class<?> type = field.getGenericKeyType();
			if (type == String.class) {
				return "";
			}

			if (! field.isPacked()) {
				// version2 默认false
				return "";
			}

			if (type.isEnum() && GeneratorProtoFeature.ENUM_TO_INT.prepare()) {
				type = int.class;
			}

			if (FieldInfo.isPrimitiveType(type)
			 || type == Boolean.class
			 || type == Integer.class
			 || type == Long.class
			) {
				return " [packed = true]";
			}

			return super.getRepeatedFieldDescribe(field);
		}
	},

	V3 {
		/** The Constant V3_HEADER. */
		private static final String V3_HEADER = "syntax=\"proto3\";\n\n";
		@Override
		public StringBuilder fileContentStringBuffer(boolean needImportCommonProto) {
			StringBuilder sb = new StringBuilder(V3_HEADER);
			if (needImportCommonProto) {
				sb.append(IMPORT_COMMON_PROTO);
			}
			if (GeneratorProtoFeature.DEFAULT_PROTO_PACKAGE.prepare()) {
				sb.append(PACKAGE_MESSAGE);
			}
			return sb;
		}

		@Override
		public String getRepeatedFieldDescribe(FieldInfo field) {
			if (! field.isPacked()) {
				// version3 默认true
				return "[packed = false]";
			}
			return super.getRepeatedFieldDescribe(field);
		}
	},
	;
	public static final String PACKAGE_MESSAGE = "package proto;\n\n";
	/**
	 * 共用class的proto文件名.
	 */
	public static final String COMMON_CLASS_PROTO_FILE_NAME = "__common__.proto";
	/**
	 * import 语句
	 */
	private static final String IMPORT_COMMON_PROTO = "import \""+COMMON_CLASS_PROTO_FILE_NAME+"\";\n\n";
	/**
	 * 得到文件的string buffer
	 * @return
	 */
	public abstract StringBuilder fileContentStringBuffer(boolean needImportCommonProto);

	/**
	 * 获得 ProtoIDLGenerator
	 * @return
	 */
	public ProtoIDLGenerator getProtoIDLGenerator() {
		return new ProtoIDLGenerator(this);
	}

	/**
	 * 字段修饰符.
	 * @return
	 */
	public String getFieldDescribe() {
		return "\t";
	}
	/**
	 * list 字段修尾缀饰符.
	 * @return
	 */
	public String getRepeatedFieldDescribe(FieldInfo field) {
		return "";
	}
}
