package org.qiunet.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.apache.poi.ss.usermodel.*;
import org.qiunet.appender.*;
import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;
import org.qiunet.frame.setting.Setting;
import org.qiunet.frame.setting.SettingManager;

import java.io.*;
import java.util.Iterator;

/**
 * @author  qiunet.
 * 17/5/25
 */
public class ExcelToCfg {
	/***
	 * excel的根目录
	 */
	private String rootPath = SettingManager.getInstance().getFirstExcelPath();
	/***
	 * 配置
	 */
	private Setting setting = SettingManager.getInstance().getSetting();
	/**
	 * 定义数据的行数-行号
	 */
	private static final int DATA_DEFINE_ROW = 4;
	/***
	 * 从根目录开始的文件名
	 */
	private String fileRelativeName;

	private File sourceFile;
	private TextArea console;

	public ExcelToCfg(File file, TextArea console) {
		this.fileRelativeName = file.getAbsolutePath().substring(rootPath.length() + 1);
		this.sourceFile = file;
		this.console = console;

	}
	/***
	 * 得到最后一行的数据
	 * @param sheet
	 * @return
	 */
	private int getSheetLastRow(Sheet sheet) {
		int lastRow = DATA_DEFINE_ROW - 1;
		for (int i = lastRow; i <= sheet.getLastRowNum(); lastRow = i, i++) {
			Row row = sheet.getRow(i);
			if (row == null || row.getCell(0) == null) {
				break;
			}
			Cell cell = row.getCell(0);
			cell.setCellType(CellType.STRING);
			String var = cell.getStringCellValue();
			if (var == null || var.trim().length() == 0) {
				break;
			}
		}
		return lastRow + 1;
	}

	/***
	 * 得到cell的长度
	 * @param sheet
	 * @return
	 */
	private int getCellLength(Sheet sheet) {
		//数据类型行
		Row dateTypeRow = sheet.getRow(DATA_DEFINE_ROW - 1);
		int cellLength = dateTypeRow.getLastCellNum();
		for (int i = 1; i < cellLength; i++) {
			Cell cell = dateTypeRow.getCell(i);
			if (cell == null) {
				cellLength = i;
				break;
			}
			cell.setCellType(CellType.STRING);
			DataType dataType = DataType.parse(cell.getStringCellValue().toLowerCase());
			if (dataType == null) {
				cellLength = i;
				break;
			}
		}
		return cellLength;
	}

	private void SheetToStream(Sheet sheet, AppenderAttachable attachable) throws Exception {
		/*设定excel 行数据的规则
		 * 第一行：说明	        		实际对应的row 为 0 加下划线_{en_name} 英文名表示需要的意思
		 * 第二行：数据类型				    实际对应的row 为 1
		 */
		int rowNum = 0, columnNum = 0;
		int lastRow = getSheetLastRow(sheet);
		this.sendMessage("表格["+fileRelativeName+"]:[" + sheet.getSheetName() + "]转换数据内容[ "+(lastRow - DATA_DEFINE_ROW)+" ]行");
		/*写二进制文件规则：参数-数据行数-数据   写流的时候 d要压缩*/

		try {
			int cellLength = getCellLength(sheet);
			// 对字段的说明
			Row descRow = sheet.getRow(0);
			//数据类型行
			Row dateTypeRow = sheet.getRow(1);

			Row dataNameRow = sheet.getRow(2);

			Row dataOutPutTypeRow = sheet.getRow(3);

			for (rowNum = DATA_DEFINE_ROW; rowNum < lastRow; rowNum++) {
				Row row = sheet.getRow(rowNum);
				for (columnNum = 0; columnNum < cellLength; columnNum++) {
					DataType dateType = DataType.parse(dateTypeRow.getCell(columnNum).getStringCellValue());
					if (dateType == null) {
						this.alterError("Sheet: [" + sheet.getSheetName() + "] Row: ["+(rowNum + 1)+"] Column ["+(columnNum + 1)+"] 数据格式不支持");
						return;
					}

					Cell c = row.getCell(columnNum);
					if (c == null) {
						c = row.createCell(columnNum);
					}
					c.setCellType(CellType.STRING);

					// 名称
					String dataName = dataNameRow.getCell(columnNum).getStringCellValue();

					OutPutType outPutType = OutPutType.valueOf(dataOutPutTypeRow.getCell(columnNum).getStringCellValue());
					attachable.append(new AppenderData(dateType, dataName, c.getStringCellValue().trim(), outPutType));
				}
				// 行结束
				attachable.rowRecordOver();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.alterError("Sheet: [" + sheet.getSheetName() + "] Row: ["+(rowNum + 1)+"] Column ["+(columnNum + 1)+"] 错误!");
		}
	}

	/***
	 * 所有的sheet
	 */
	public void excelToStream() {
		try {
			//能自动识别excel版本
			Workbook workbook = WorkbookFactory.create(new FileInputStream(sourceFile));
			String fileNamePrefix = sourceFile.getName().substring(sourceFile.getName().indexOf("_") + 1, sourceFile.getName().indexOf("."));
			String relativeDirPath = sourceFile.getParentFile().getAbsolutePath().substring(rootPath.length());

			AppenderAttachable appenderAttachable = new AppenderAttachable(sourceFile.getName());
			if (setting.isXdChecked()){
				appenderAttachable.addAppender(new XdAppender(relativeDirPath, fileNamePrefix));
			}
			if (setting.isJsonChecked()) {
				appenderAttachable.addAppender(new JsonAppender(relativeDirPath, fileNamePrefix));
			}
			if (setting.isXmlChecked()) {
				appenderAttachable.addAppender(new XmlAppender(relativeDirPath, fileNamePrefix));
			}

			if (appenderAttachable.getAppenderSize() == 0){
				this.alterError("请选择至少一种配置输出格式");
				return;
			}

			for (Iterator<Sheet> it = workbook.sheetIterator(); it.hasNext(); ) {
				//分页片
				Sheet sheet = it.next();
				if ("end".equals(sheet.getSheetName())){
					break;
				}

				try {
					this.SheetToStream(sheet, appenderAttachable);
				} catch (Exception e) {
					e.printStackTrace();
				}

				appenderAttachable.sheetOver(sheet.getSheetName());
			}

			appenderAttachable.fileOver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.sendMessage("["+this.fileRelativeName +"]导出配置成功");
	}


	private void alterError(String message) {
		FxUIUtil.openAlert(Alert.AlertType.ERROR, message, "错误");
	}
	private void sendMessage(String message) {
		FxUIUtil.sendMsgToTextInput(console, message, true);
	}
}
