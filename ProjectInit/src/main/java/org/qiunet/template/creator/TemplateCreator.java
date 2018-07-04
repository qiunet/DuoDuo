package org.qiunet.template.creator;

import org.qiunet.project.init.ProjectInitData;

/**
 * @author qiunet
 *         Created on 16/11/16 20:39.
 */
public class TemplateCreator<VmElement extends org.qiunet.template.parse.xml.VmElement> {
	private BaseXmlParse<VmElement> parse;

	private ProjectInitData initData;

	private VmElement vmElement;

	public void addVmElement(VmElement vmElement){
		this.vmElement = vmElement;
	}

	public TemplateCreator(BaseXmlParse<VmElement> parse, ProjectInitData initData) {
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
		// 调用parsevm 生成文件
		vmElement.parseVm(initData);

		return vmElement;
	}
}
