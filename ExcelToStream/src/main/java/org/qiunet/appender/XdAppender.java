package org.qiunet.appender;



import org.qiunet.utils.DataType;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * 拼接xd的方式
 * Created by qiunet.
 * 17/10/30
 */
public class XdAppender implements Appender {
	private String outFileParent;

	private String filePrefix;

	private DataOutputStream dos;
	private ByteArrayOutputStream baos;

	public XdAppender(String outFileParent, String filePrefix) {
		this.filePrefix = filePrefix;
		this.outFileParent = outFileParent;
	}

	@Override
	public void rowRecordOver() {
		// do nothing
	}

	@Override
	public void recordNum(int count) {
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);

			dos.writeInt(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void append(DataType datatype, String name, String val, boolean cliFlag) {
		try {
			switch (datatype) {
				case DATA_INT:
					dos.writeInt(Integer.parseInt(val));
				break;
				case DATA_DOUBLE:
					dos.writeDouble(Double.parseDouble(val));
					break;
				case DATA_STRING:
					dos.writeUTF(val);
					break;
				default:
					throw new IllegalArgumentException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sheetOver(String sheetName) {
		if (sheetName.startsWith("_")) sheetName = sheetName.substring(1, sheetName.length());

		FileOutputStream outStream = null;
		DataOutputStream dos = null;
		try {
			File outFile = new File(outFileParent, filePrefix+"_"+ sheetName + ".xd");
			outStream = new FileOutputStream(outFile);
			dos = new DataOutputStream(outStream);

			dos.write(this.baos.toByteArray());

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(dos != null) dos.close();
				if (outStream != null) outStream.close();

				if (this.baos != null) this.baos.close();
				if (this.dos != null) this.dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void fileOver() {
		// do noting
	}
}
