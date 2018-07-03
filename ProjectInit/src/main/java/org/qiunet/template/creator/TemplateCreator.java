package org.qiunet.template.creator;

import org.qiunet.project.init.ProjectInitData;

/**
 * @author qiunet
 *         Created on 16/11/16 20:39.
 */
public class TemplateCreator<VmElement extends org.qiunet.template.parse.xml.VmElement, XmlParse extends BaseXmlParse<VmElement>> {
	private XmlParse parse;

	private ProjectInitData initData;

	private VmElement vmElement;

	public void addVmElement(VmElement vmElement){
		this.vmElement = vmElement;
	}

	public TemplateCreator(XmlParse parse, ProjectInitData initData) {
		this.parse = parse;
		this.initData = initData;
	}
	/***
	 * 输出base
	 */
	public VmElement parseTemplate() {
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
