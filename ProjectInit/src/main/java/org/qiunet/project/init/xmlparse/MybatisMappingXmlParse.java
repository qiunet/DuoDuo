package org.qiunet.project.init.xmlparse;

import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.project.init.elements.mapping.ElementMapping;

/**
 * @author qiunet
 *         Created on 17/2/23 18:15.
 */
public class MybatisMappingXmlParse extends BaseXmlParse {
	public MybatisMappingXmlParse(String basePath, String xmlConfigPath) {
		super(basePath, xmlConfigPath);
	}

	@Override
	public void parseXml() {
		this.addObjectCreate("base/mapping", ElementMapping.class);
	}
}
