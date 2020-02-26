package org.qiunet.excel2cfgs.appender;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.qiunet.excel2cfgs.enums.RoleType;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/***
 *
 *
 * @author  qiunet
 * 2019-11-25 15:20
 ***/
public class XmlAppender extends BaseAppender {

	public XmlAppender(String relativeDirPath, String filePrefix) {
		super(relativeDirPath, filePrefix);
	}

	@Override
	public void createCfgFile(String sheetName, RoleType roleType, String outPath, AppenderAttachable attachable) {
		List<List<AppenderData>> appenderDatas = attachable.getAppenderDatas();
		if (appenderDatas.isEmpty()) {
			return;
		}

		Path path = Paths.get(outPath, outputRelativePath, filePrefix + "_" + sheetName + ".xml");
		if (! path.toFile().getParentFile().exists()) {
			path.toFile().getParentFile().mkdirs();
		}
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("configs");

		for (List<AppenderData> rowDatas : appenderDatas) {
			Element config = root.addElement("config");
			for (AppenderData rowData : rowDatas) {
				if (rowData.getOutPutType().canWrite(roleType)) {
					Element attr = config.addElement(rowData.getName());
					attr.addText(rowData.getVal());
				}
			}
		}

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");

		try(FileOutputStream fos = new FileOutputStream(path.toFile())) {
			XMLWriter xmlWriter = new XMLWriter(fos, format);
			xmlWriter.write(document);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.copyToProject(path.toFile());
		}
	}

	@Override
	public void fileOver() {

	}

	@Override
	public String name() {
		return "xml";
	}
}
