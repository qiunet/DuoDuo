package org.qiunet.utils;

import org.apache.poi.ss.usermodel.*;
import org.qiunet.appender.AppenderAttachable;
import org.qiunet.appender.CliJsonAppender;
import org.qiunet.appender.XdAppender;
import org.qiunet.exception.ExchangeException;
import org.qiunet.fx.bean.ConfigData;
import org.qiunet.fx.common.ConfigManager;
import org.qiunet.utils.string.StringUtil;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qiunet.
 * 17/5/25
 */
public class ExcelToStream {
	/**
	 * 定义数据的行数-行号
	 */
	private static final int DATA_DEFINE_ROW = 2;

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
		Row dateTypeRow = sheet.getRow(DATA_DEFINE_ROW - 1);                                    //数据类型行
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
		System.out.print("File:[" + attachable.getFileName() + "] Sheet:[" + sheet.getSheetName() + "]---------LASTROW:");
		/*设定excel 行数据的规则
		 * 第一行：说明	        		实际对应的row 为 0 加下划线_{en_name} 英文名表示需要的意思
		 * 第二行：数据类型				    实际对应的row 为 1
		 */
		int rowNum = 0, columnNum = 0;
		int lastRow = getSheetLastRow(sheet);
		System.out.println(lastRow + "  数据行数:" + (lastRow - DATA_DEFINE_ROW));
		/*写二进制文件规则：参数-数据行数-数据   写流的时候 d要压缩*/

		try {
			// 记录总行数
			attachable.recordNum(lastRow - DATA_DEFINE_ROW);                                                //数据行数

			int cellLength = getCellLength(sheet);
			Row dateTypeRow = sheet.getRow(DATA_DEFINE_ROW - 1);                                    //数据类型行
			Row nameRow = sheet.getRow(0);                                                        // 对字段的说明

			for (rowNum = DATA_DEFINE_ROW; rowNum < lastRow; rowNum++) {
				Row row = sheet.getRow(rowNum);
				for (columnNum = 0; columnNum < cellLength; columnNum++) {
					DataType dateType = DataType.parse(dateTypeRow.getCell(columnNum).getStringCellValue());

					if (dateType == null) {
						throw new IllegalArgumentException("File:[" + attachable.getFileName() + "] Sheet: [" + sheet.getSheetName() + "] rowNum[" + (rowNum + 1) + "], columnNum[" + (columnNum + 1) + "] dateType error : " + dateTypeRow.getCell(columnNum).getStringCellValue());
					}

					Cell c = row.getCell(columnNum);
					if (c == null) c = row.createCell(columnNum);
					c.setCellType(CellType.STRING);

					String name = nameRow.getCell(columnNum).getStringCellValue();

					String dataName = ""; // 名称
					boolean cliFlag = name.indexOf("_") > 0;     // 客户端的flag
					if (cliFlag) {
						dataName = name.substring(name.indexOf("_") + 1, name.length());
					}

					attachable.append(dateType, dataName, c.getStringCellValue().trim(), cliFlag);

				}
				// 行结束
				attachable.rowRecordOver();
			}
		} catch (NumberFormatException e) {
			throw new ExchangeException("File:[" + attachable.getFileName() + "] Sheet: [" + sheet.getSheetName() + "]", rowNum + 1, columnNum + 1, e.getMessage());
		}
	}

	/***
	 * 所有的sheet
	 * @param sourceFile
	 */
	public String excelToStream(File sourceFile) {
		try {
			Workbook workbook = WorkbookFactory.create(new FileInputStream(sourceFile));//能自动识别excel版本
			String fileNamePrefix = sourceFile.getName().substring(sourceFile.getName().indexOf("_") + 1, sourceFile.getName().indexOf("."));
			String parentPath = sourceFile.getParent();
			ConfigData configData =ConfigManager.getInstance().getData(ConfigManager.getInstance().getLast_check_excel());
			String projectPath = configData.getFirstOutPath();

			String jsonConfigPath = projectPath + File.separator + ".json.config";
			String configPath = projectPath + File.separator + ".xd.config";

			if (!parentPath.equals(projectPath)) configPath += parentPath.substring(projectPath.length());

			File dir = new File(configPath);
			if (!dir.exists()) dir.mkdirs();

			AppenderAttachable appenderAttachable = new AppenderAttachable(sourceFile.getName());
			if (configData.isXd())
				appenderAttachable.addAppender(new XdAppender(configPath, fileNamePrefix));
			if (configData.isJson())
				appenderAttachable.addAppender(new CliJsonAppender(jsonConfigPath, fileNamePrefix));
			//xml 的appender
			//if(configData.isXml())
			if (appenderAttachable.getAppenderSize() == 0)
				return "请选择至少一个appender";

			for (Iterator<Sheet> it = workbook.sheetIterator(); it.hasNext(); ) {
				Sheet sheet = it.next();        //分页片
				if ("end".equals(sheet.getSheetName())) break;


				try {
					this.SheetToStream(sheet, appenderAttachable);
				} catch (Exception e) {
					e.printStackTrace();
					return e.getMessage();
				}

				appenderAttachable.sheetOver(sheet.getSheetName());
			}

			appenderAttachable.fileOver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String allExcelToStream(List<File> files) {
		final StringBuffer sb = new StringBuffer();
		files.forEach(file -> {
			String msg = excelToStream(file);
			if (!StringUtil.isEmpty(msg)) sb.append(msg).append("\r\n");
		});
		if (sb.length() > 0)
			return sb.toString();
		return null;
	}
}
