package org.qiunet.project.init.xmlparse;

import org.qiunet.project.init.elements.mapping.MappingVmElement;
import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.project.init.elements.mapping.ElementMapping;

/**
 * @author qiunet
 *         Created on 17/2/23 18:15.
 */
public class MybatisMappingXmlParse extends BaseXmlParse<MappingVmElement> {

	public MybatisMappingXmlParse(String xmlConfigPath) {
		super(MappingVmElement.class, xmlConfigPath);
	}

	@Override
	public void parseXml() {
		this.addObjectCreate("base/mapping", ElementMapping.class);
	}
}
