package org.qiunet.appender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class JsonAppender implements IAppender {

	private String filePrefix;

	private JSONArray rowRecordArray;

	private JSONObject rowRecord;
	/**
	 * 相对根目录的目录相对路径
	 */
	private String relativeDirPath;
	public JsonAppender(String relativeDirPath, String filePrefix) {
		this.relativeDirPath = relativeDirPath;
		this.rowRecordArray = new JSONArray();
		this.rowRecord = new JSONObject();
		this.filePrefix = filePrefix;

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
	public void append(DataType datatype, String name, String val, OutPutType outPutType) {
		if (rowRecord == null) {
			rowRecord = new JSONObject();
		}

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
			case DATA_LONG:
				this.rowRecord.put(name, Long.parseLong(val));
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
			Path path = Paths.get(getServerOutputPath(), relativeDirPath, filePrefix + "_" + sheetName + ".json");
			if (! path.toFile().getParentFile().exists()) {
				path.toFile().getParentFile().mkdirs();
			}
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			try {
				fos = new FileOutputStream(path.toFile());
				writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				writer.write(rowRecordArray.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if (writer != null) {
						writer.close();
					}
					if (fos != null) {
						fos.close();
					}
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

	@Override
	public String name() {
		return "json";
	}
}
