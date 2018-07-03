package org.qiunet.template.parse.xml;

import org.qiunet.project.init.IProjectInitConfig;
import org.qiunet.project.init.ProjectInitData;
import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.mapping.ElementMapping;
import org.qiunet.template.parse.template.VelocityFactory;

import java.io.File;
import java.util.List;

/**
 * 可以输出vm模板的最小单位. 下面的数据结构自行定义.
 * @author qiunet
 *         Created on 16/11/21 11:54.
 */
public abstract class SubVmElement<T extends VmElement> {
	protected T vmElement;

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

		vmElement.getInitData().setCurrData(this);

		VelocityFactory.getInstance().parseOutFile(vmfilePath, outputFileName, vmElement.getInitData());
	}
	/**
	 * 得到输出文件的路径
	 * @return
	 */
	protected abstract String getOutFilePath();

	protected Entity getEntity(String poName){
		return vmElement.getInitData().getEntity(poName);
	}

	protected List<ElementMapping> getAllElementMapping() {
		return vmElement.getInitData().getAllElementMapping();
	}

	protected IProjectInitConfig getProjectConfig() {
		return vmElement.getInitData().getConfig();
	}
	/**
	 * 给每个subElement 设置 vmElement. 这样 subElement能取到ProjectInitData的数据
	 * @param vmElement VmElement
	 */
	void setVmElement(T vmElement) {
		this.vmElement = vmElement;
	}
}
