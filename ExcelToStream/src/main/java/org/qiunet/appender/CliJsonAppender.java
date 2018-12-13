package org.qiunet.appender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.utils.DataType;

import java.io.*;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class CliJsonAppender implements Appender{
	private String outFileParent;

	private String filePrefix;

	private JSONArray rowRecordArray;

	private JSONObject rowRecord;

	public CliJsonAppender(String outFileParent, String filePrefix) {
		this.filePrefix = filePrefix;
		this.outFileParent = outFileParent;

		this.rowRecordArray = new JSONArray();
		this.rowRecord = new JSONObject();
	}

	@Override
	public void rowRecordOver() {
		if (! this.rowRecord.isEmpty()){
			this.rowRecordArray.add(this.rowRecord);
			this.rowRecord = null;
		}
	}

	@Override
	public void recordNum(int count) {
		this.rowRecordArray = new JSONArray(count);
	}

	@Override
	public void append(DataType datatype, String name, String val, boolean cliFlag) {
		if (rowRecord == null) rowRecord = new JSONObject();

		if ( !cliFlag ) return;

		switch (datatype) {
			case DATA_STRING:
				this.rowRecord.put(name, val);
				break;
			case DATA_DOUBLE:
				this.rowRecord.put(name, Double.parseDouble(val));
				break;
			case DATA_INT:
				this.rowRecord.put(name, Integer.parseInt(val));
				break;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public void sheetOver(String sheetName) {
		// 客户端约定俗成的 _ 开始不记录
		if (sheetName.startsWith("_")) {
			System.out.println("CliJson continue sheet ["+sheetName+"]");
			return;
		}
		if (! this.rowRecordArray.isEmpty()){
			File outFile = new File(outFileParent);
			if (! outFile.exists()) outFile.mkdirs();
			outFile = new File(outFileParent, filePrefix+"_"+sheetName+".json");

			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			try {
				fos = new FileOutputStream(outFile);
				writer = new OutputStreamWriter(fos, "UTF-8");
				writer.write(rowRecordArray.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if (writer != null)	writer.close();
					if (fos != null)	fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.rowRecordArray = null;
	}

	@Override
	public void fileOver() {

	}
}
