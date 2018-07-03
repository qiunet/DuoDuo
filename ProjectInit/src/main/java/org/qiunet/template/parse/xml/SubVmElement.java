package org.qiunet.template.parse.xml;

import org.qiunet.project.init.IProjectInitConfig;
import org.qiunet.project.init.ProjectInitData;
import org.qiunet.template.parse.template.VelocityFactory;

import java.io.File;

/**
 * 可以输出vm模板的最小单位. 下面的数据结构自行定义.
 * @author qiunet
 *         Created on 16/11/21 11:54.
 */
public abstract class SubVmElement {
	protected ProjectInitData initData;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void parseOutFile(String outfilePath, String vmfilePath, String postfix) {
		if (! outfilePath.endsWith(File.separator))  outfilePath += File.separator;

		String outputFileName = outfilePath + name +"."+ postfix;

		VelocityFactory.getInstance().parseOutFile(vmfilePath, outputFileName, this);
	}
	/**
	 * 得到输出文件的路径
	 * @return
	 */
	protected abstract String getOutFilePath();

	protected IProjectInitConfig getProjectConfig() {
		return initData.getConfig();
	}
	/**
	 * 给每个subElement 设置 ProjectInitData. 这样 subElement能取到ProjectInitData的数据
	 * @param initData ProjectInitData
	 */
	void setInitData(ProjectInitData initData) {
		this.initData = initData;
	}
}
