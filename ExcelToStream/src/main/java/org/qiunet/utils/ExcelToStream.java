package org.qiunet.utils;

import org.apache.poi.ss.usermodel.*;
import org.qiunet.exception.ExchangeException;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

/**
 * Created by qiunet.
 * 17/5/25
 */
public class ExcelToStream {
	private static final Set<String> typeSet = new HashSet<String>(Arrays.asList(new String[]{"string" , "double" , "int"}));
	/**定义数据的行数-行号*/
	private static final int DATA_DEFINE_ROW = 2;

	/***
	 * 得到最后一行的数据
	 * @param sheet
	 * @return
	 */
	private int getSheetLastRow(Sheet sheet) {
		int lastRow = DATA_DEFINE_ROW;
		for (int i = DATA_DEFINE_ROW; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(0);
			if(cell == null){
				lastRow = i;
				break;
			}
			cell.setCellType(CellType.STRING);
			String var = cell.getStringCellValue();
			if(var == null || var.trim().length() == 0){
				lastRow = i;
				break;
			}
				/*结尾*/
			if(i == sheet.getLastRowNum()){
				lastRow = i+1;
			}
		}
		return lastRow;
	}

	/***
	 * 得到cell的长度
	 * @param sheet
	 * @return
	 */
	private int getCellLength (Sheet sheet){
		Row dateTypeRow = sheet.getRow(DATA_DEFINE_ROW-1);									//数据类型行
		int cellLength = dateTypeRow.getLastCellNum();
		for(int i = 1; i < cellLength; i++){
			Cell cell = dateTypeRow.getCell(i);
			if(cell == null ){
				cellLength = i;
				break;
			}
			cell.setCellType(CellType.STRING);
			String dataType = cell.getStringCellValue().toLowerCase();
			if(! typeSet.contains(dataType)){
				cellLength = i;
				break;
			}
		}
		return cellLength;
	}

	private void SheetToStream(Sheet sheet , File outFile) throws Exception {
			/*设定excel 行数据的规则
			 * 第一行：版本(兼容付总的)			实际对应的row 为 0
			 * 第二行：参数   				实际对应的row 为 1
			 * 第三行：数据内容说明-不用管		实际对应的row 为 2
			 * 第四行：数据类型				实际对应的row 为 3
			 */
		int rowNum = 0,columnNum = 0;
		int lastRow = getSheetLastRow(sheet);
		System.out.println("---------LASTROW:"+lastRow + "  数据行数:"+(lastRow-DATA_DEFINE_ROW));
			/*写二进制文件规则：参数-数据行数-数据   写流的时候 d要压缩*/

		FileOutputStream outStream = null;
		GZIPOutputStream gos = null;
		DataOutputStream dos = null;
		try {
			outStream = new FileOutputStream(outFile);
			gos = new GZIPOutputStream(outStream);
			dos = new DataOutputStream(gos);
			dos.writeInt(lastRow-DATA_DEFINE_ROW );												//数据行数
			int cellLength = getCellLength(sheet);
			Row dateTypeRow = sheet.getRow(DATA_DEFINE_ROW-1);									//数据类型行
			for (rowNum = DATA_DEFINE_ROW; rowNum < lastRow; rowNum++) {
				Row row = sheet.getRow(rowNum);
				for (columnNum = 0; columnNum < cellLength; columnNum++) {
					String dateType = dateTypeRow.getCell(columnNum).getStringCellValue().toLowerCase();
					Cell c = row.getCell(columnNum);

					if(c == null) c = row.createCell(columnNum);

					c.setCellType(CellType.STRING);

					if ("int".equals(dateType)) {
						dos.writeInt(Integer.parseInt(c.getStringCellValue()));
					}else if ("double".equals(dateType)) {
						dos.writeDouble(Double.parseDouble(c.getStringCellValue()));
					}else if("string".equals(dateType)){
						dos.writeUTF(c.getStringCellValue());
					}else{
						throw new IllegalArgumentException("dateType error : " +dateType);
					}
				}
			}
		}catch (NumberFormatException e) {
			throw new ExchangeException(rowNum+1, columnNum+1);
		} finally {
			try {
				dos.close();
				gos.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/***
	 * 所有的sheet
	 * @param sourceFile
	 */
	public String excelToStream(File sourceFile) {
		try {
			Workbook workbook = WorkbookFactory.create(new FileInputStream(sourceFile));//能自动识别excel版本
			String fileNamePrefix = sourceFile.getName().substring(0, sourceFile.getName().indexOf("."));
			for (Iterator<Sheet> it = workbook.sheetIterator(); it.hasNext(); ){
				Sheet sheet = it.next();		//分页片
				if ("end".equals(sheet.getSheetName())) break;

				String newFileName = fileNamePrefix + "_" + sheet.getSheetName() + ".xd";
				File outFile = new File(sourceFile.getParent(), newFileName);
				try {
					this.SheetToStream(sheet, outFile);
				}catch (Exception e) {
					e.printStackTrace();
					outFile.delete();
					return e.getMessage();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
