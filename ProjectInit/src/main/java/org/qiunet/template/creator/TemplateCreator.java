package org.qiunet.template.creator;

import org.qiunet.project.init.ProjectInitData;
import org.qiunet.template.parse.template.VelocityFactory;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;

/**
 * @author qiunet
 *         Created on 16/11/16 20:39.
 */
public class TemplateCreator<T extends SubVmElement> {
	private BaseXmlParse parse;

	private ProjectInitData initData;

	private VmElement<T> vmElement;

	public void addVmElement(VmElement<T> vmElement){
		this.vmElement = vmElement;
	}

	public TemplateCreator(BaseXmlParse parse, ProjectInitData initData) {
		this.parse = parse;
		this.initData = initData;
	}
	/***
	 * 输出base
	 */
	public VmElement<T> parseTemplate() {
		parse.setValidating(false);
		parse.push(this ,"addVmElement");
		parse.parseXml();
		parse.parse();
		// 是生成VmElement 后才能设置initData
		this.vmElement.initData(initData);
		// 调用parsevm 生成文件
		vmElement.parseVm(parse.getBasePath());

		return vmElement;
	}
}
