package org.qiunet.appender;



import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 拼接xd的方式
 * Created by qiunet.
 * 17/10/30
 */
public class XdAppender implements IAppender {

	private String filePrefix;

	private DataOutputStream dos;

	private String outputRelativePath;

	private ByteArrayOutputStream baos;

	public XdAppender(String outputRelativePath, String filePrefix) {
		this.outputRelativePath = outputRelativePath;
		this.filePrefix = filePrefix;
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
	public void append(DataType datatype, String name, String val, OutPutType outPutType) {
		try {
			datatype.writeData(dos, val);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sheetOver(String sheetName) {
		if (sheetName.startsWith("_")) {
			sheetName = sheetName.substring(1);
		}

		FileOutputStream outStream = null;
		DataOutputStream dos = null;
		try {
			Path path = Paths.get(getServerOutputPath(), outputRelativePath, filePrefix + "_" + sheetName + ".xd");
			if (! path.toFile().getParentFile().exists()) {
				path.toFile().getParentFile().mkdirs();
			}
			outStream = new FileOutputStream(path.toFile());
			dos = new DataOutputStream(outStream);

			dos.write(this.baos.toByteArray());

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(dos != null) {
					dos.close();
				}
				if (outStream != null) {
					outStream.close();
				}

				if (this.baos != null) {
					this.baos.close();
				}
				if (this.dos != null) {
					this.dos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
