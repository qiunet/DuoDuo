package org.qiunet.excel2cfgs.appender;

import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class ProtoAppender extends BaseAppender {

	public ProtoAppender(File sourceFile, String relativeDirPath, String filePrefix) {
		super(sourceFile, relativeDirPath, filePrefix);
	}

	@Override
	public void createCfgFile(String sheetName, RoleType roleType, String outPath, AppenderAttachable attachable) {
		File file = Paths.get(outPath, outputRelativePath, filePrefix + ".proto").toFile();
		if (! file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		StringBuilder sb = new StringBuilder();
		if (! file.exists() || file.length() == 0) {
			sb.append("syntax = \"proto3\";\n\n");
		}
		sb.append("// ").append(outputRelativePath).append(sourceFile.getName()).append(" Sheet[").append(sheetName).append("]\n");
		sb.append("message ").append(filePrefix).append("_").append(sheetName).append("{\n");
		List<NameAppenderData> rowNames = attachable.getRowNames(roleType);
		for (int i = 0; i < rowNames.size(); i++) {
			NameAppenderData d = rowNames.get(i);
			sb.append("\t// ").append(d.desc).append("\n");
			sb.append("\t").append(d.dataType.getProtoType()).append(" ").append(d.name).append(" = ").append(i+1).append(";\n");
		}
		sb.append("}\n\n");
		FileUtil.appendToFile(file, sb.toString());
	}

	@Override
	public void fileOver() {

	}

	@Override
	public String name() {
		return "proto";
	}
}
