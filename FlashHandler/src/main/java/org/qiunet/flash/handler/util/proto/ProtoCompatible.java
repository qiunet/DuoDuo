package org.qiunet.flash.handler.util.proto;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/***
 * 解析proto 判断是否兼容.
 *
 * @author qiunet
 * 2022/11/11 13:58
 */
public class ProtoCompatible {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 所有proto的详情
	 */
	private final Map<String, ProtoDetailInfo> protoDetailInfoMap = Maps.newHashMap();

	private final File dir;

	public ProtoCompatible(File dir) {
		Preconditions.checkState(dir.isDirectory());
		File[] files = dir.listFiles();
		this.dir = dir;
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (! file.getName().endsWith( ".proto")) {
				continue;
			}

			this.protoDetailInfoMap.put(file.getName(), new ProtoDetailInfo(file));
		}
	}

	public File getDir() {
		return dir;
	}

	/**
	 * 是否兼容
	 * 当前的对象为生成前的.
	 * 对比新的对象
	 * info 新的对象
	 */
	public boolean compatible(ProtoCompatible protoCompatible) {
		for (Map.Entry<String, ProtoDetailInfo> entry : this.protoDetailInfoMap.entrySet()) {
			ProtoDetailInfo info = protoCompatible.protoDetailInfoMap.get(entry.getKey());
			if (info == null) {
				logger.error("proto file [{}] not exist! not compatible!", entry.getKey());
				return false;
			}

			if (! entry.getValue().compatible(info)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 每个proto的详情
	 */
	public static class ProtoDetailInfo {
		private final Map<String, MessageInfo> msgMapping = Maps.newHashMap();

		private final File file;
		public ProtoDetailInfo(File file) {
			List<String> lines = FileUtil.readFileLines(file);
			this.file = file;
			int startLine = 0;
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				// 不会null
				line = line.trim();
				if (StringUtil.isEmpty(line)) {
					continue;
				}
				// 注释
				if (line.startsWith("//")) {
					continue;
				}

				if (line.startsWith("message") || line.startsWith("enum")) {
					startLine = i;
				}

				if (line.equals("}")) {
					MessageInfo messageInfo = new MessageInfo(this, lines.subList(startLine, i));
					msgMapping.put(messageInfo.name, messageInfo);
				}
			}
		}

		boolean compatible(ProtoDetailInfo info) {
			for (Map.Entry<String, MessageInfo> en : this.msgMapping.entrySet()) {
				MessageInfo messageInfo = info.msgMapping.get(en.getKey());
				if (messageInfo == null) {
					logger.error("File {} message {} deleted. not compatible!",
							this.file.getName(), en.getKey());
					return false;
				}
				if (! en.getValue().compatible(messageInfo)) {
					return false;
				}
			}
			return true;
		}
	}

	private static class MessageInfo {
		/**
		 * message 里面的字段列表
		 */
		private final Map<Integer, FieldInfo> fieldMap = Maps.newHashMap();

		private final ProtoDetailInfo protoDetailInfo;
		/**
		 * 是否枚举类型
		 */
		private boolean enumType;
		/**
		 * message名称
		 */
		private String name;

		public MessageInfo(ProtoDetailInfo protoDetailInfo, List<String> lines) {
			this.protoDetailInfo = protoDetailInfo;
			for (String line : lines) {
				line = line.trim();

				if (StringUtil.isEmpty(line) || line.startsWith("//") || line.equals("}")) {
					continue;
				}

				if (line.startsWith("message") || line.startsWith("enum")) {
					String[] strings = StringUtil.split(line.substring(0, line.indexOf("{")), " ");
					this.enumType = line.contains("enum");
					this.name = strings[1];
					continue;
				}
				FieldInfo fieldInfo = new FieldInfo(this, line, this.enumType);
				this.fieldMap.put(fieldInfo.order, fieldInfo);
			}
		}

		boolean compatible(MessageInfo info) {
			if (this.enumType != info.enumType || ! Objects.equals(this.name, info.name)) {
				logger.error("File {} message {} type change. not compatible!",
						this.protoDetailInfo.file.getName(), name);
				return false;
			}
			for (Map.Entry<Integer, FieldInfo> entry : this.fieldMap.entrySet()) {
				FieldInfo fieldInfo = info.fieldMap.get(entry.getKey());
				if (fieldInfo == null) {
					logger.error("File {} message {} field [name:{}-order:{}] deleted. not compatible!",
							this.protoDetailInfo.file.getName(), name, entry.getValue().name, entry.getKey());
					return false;
				}

				if (! entry.getValue().compatible(fieldInfo)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 字段的信息
	 */
	private static class FieldInfo {
		private static final String optional_key = "optional";
		private static final String repeated_key = "repeated";

		private final MessageInfo messageInfo;
		/**
		 * 是否可选
		 */
		private boolean optional;
		/**
		 * 是否列表
		 */
		private boolean repeated;
		/**
		 * 类型
		 */
		private String type;
		/**
		 * 名字
		 */
		private final String name;
		/**
		 * 序列
		 */
		private final int order;

		private FieldInfo(MessageInfo messageInfo, String line, boolean enumType) {
			// 去掉最后的 ;
			line = line.trim().substring(0, line.length() - 1);
			this.messageInfo = messageInfo;

			if (line.trim().startsWith(optional_key)) {
				line = line.substring(optional_key.length() + 1);
				this.optional = true;
			}

			if (line.trim().startsWith(repeated_key)) {
				line = line.substring(repeated_key.length() + 1);
				repeated = true;
			}

			String[] strings = StringUtil.split(line.trim(), "=");
			this.order = Integer.parseInt(strings[1]);
			String[] splits = StringUtil.split(strings[0], " ");
			if (! enumType) {
				this.type = splits[0];
				this.name = splits[1];
			}else {
				this.name = splits[0];
			}
		}

		boolean compatible(FieldInfo fieldInfo) {
			boolean compatible = fieldInfo.repeated == this.repeated
					&& Objects.equals(fieldInfo.type, this.type)
					&& Objects.equals(fieldInfo.name, this.name)
					&& fieldInfo.optional == this.optional
					&& fieldInfo.order == this.order;
			if (! compatible) {
				logger.error("File {} message {}.{} not compatible!", messageInfo.protoDetailInfo.file.getName(), messageInfo.name, name);
				logger.error("old: {}", this);
				logger.error("new: {}", fieldInfo);
			}
			return compatible;
		}

		@Override
		public String toString() {
			return "{" +
					"optional=" + optional +
					", repeated=" + repeated +
					", type='" + type + '\'' +
					", name='" + name + '\'' +
					", order=" + order +
					'}';
		}
	}
}
