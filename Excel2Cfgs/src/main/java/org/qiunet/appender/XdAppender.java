package org.qiunet.appender;



import javafx.scene.control.Alert;
import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;
import org.qiunet.utils.FxUIUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 拼接xd的方式
 * Created by qiunet.
 * 17/10/30
 */
public class XdAppender implements IAppender {

	private String filePrefix;

	private String outputRelativePath;


	public XdAppender(String outputRelativePath, String filePrefix) {
		this.outputRelativePath = outputRelativePath;
		this.filePrefix = filePrefix;
	}

	@Override
	public void createCfgFile(String sheetName, boolean server, String outPath, AppenderAttachable attachable) {
		Path path = Paths.get(outPath, outputRelativePath, filePrefix + "_" + sheetName + ".xd");
		if (! path.toFile().getParentFile().exists()) {
			path.toFile().getParentFile().mkdirs();
		}

		List<List<AppenderData>> appenderDatas = attachable.getAppenderDatas();
		try(FileOutputStream fos = new FileOutputStream(path.toFile());
			DataOutputStream dos = new DataOutputStream(fos)) {
			// 写入数据行数
			dos.writeInt(appenderDatas.size());
			// 写入名称
			List<String> names = attachable.getRowNames();
			dos.writeShort(names.size());
			for (String name : names) {
				dos.writeUTF(name);
			}
			// 写入数据
			for (List<AppenderData> rowDatas : appenderDatas) {
				for (AppenderData rowData : rowDatas) {
					OutPutType oType = rowData.getOutPutType();
					if (oType == OutPutType.ALL
					|| (server && oType == OutPutType.SERVER)
					|| (!server && oType == OutPutType.CLIENT)) {
						rowData.getDataType().writeData(dos, rowData.getVal());
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			FxUIUtil.openAlert(Alert.AlertType.ERROR, e.getMessage(), "错误");
		}
	}

	@Override
	public void fileOver() {
		// do noting
	}

	@Override
	public String name() {
		return "xd";
	}
}
