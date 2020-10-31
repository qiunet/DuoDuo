package org.qiunet.excel2cfgs.appender;


import javafx.scene.control.Alert;
import org.qiunet.excel2cfgs.enums.OutPutType;
import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.utils.FxUIUtil;
import org.qiunet.utils.common.CommonUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * 拼接xd的方式
 * Created by qiunet.
 * 17/10/30
 */
public class XdAppender extends BaseAppender {

	public XdAppender(File sourceFile,  String outputRelativePath, String filePrefix) {
		super(sourceFile, outputRelativePath, filePrefix);
	}

	@Override
	public void createCfgFile(String sheetName, RoleType roleType, String outPath, AppenderAttachable attachable) {
		Path path = Paths.get(outPath, outputRelativePath, filePrefix + "_" + sheetName + ".xd");
		if (! path.toFile().getParentFile().exists()) {
			path.toFile().getParentFile().mkdirs();
		}

		List<List<AppenderData>> appenderDatas = attachable.getAppenderDatas();
		try (ByteArrayOutputStream bouts = new ByteArrayOutputStream(1024)){
			try(GZIPOutputStream gos = new GZIPOutputStream(bouts);
				DataOutputStream dos = new DataOutputStream(gos)) {
				// 写入数据行数
				dos.writeInt(appenderDatas.size());

				List<NameAppenderData> rowNames = attachable.getRowNames(roleType);
				dos.writeShort(rowNames.size());
				for (NameAppenderData nameData : rowNames) {
					dos.writeUTF(nameData.getName());
				}
				// 写入数据
				for (List<AppenderData> rowDatas : appenderDatas) {
					for (AppenderData rowData : rowDatas) {
						OutPutType oType = rowData.getOutPutType();
						if (oType.canWrite(roleType)) {
							dos.writeUTF(rowData.getVal());
						}
					}
				}
			}

			byte [] bytes = bouts.toByteArray();
			CommonUtil.reverse(bytes, 2);
			try (FileOutputStream fos = new FileOutputStream(path.toFile())){
				fos.write(bytes);
			}
		} catch (RuntimeException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			FxUIUtil.openAlert(Alert.AlertType.ERROR, e.getMessage(), "错误");
		}



		this.copyToProject(path.toFile());
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
