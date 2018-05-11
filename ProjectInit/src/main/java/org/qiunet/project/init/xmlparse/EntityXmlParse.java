package org.qiunet.project.init.xmlparse;

import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.entity.Field;
import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.project.init.elements.entity.Constructor;

/**
 * @author qiunet
 *         Created on 16/11/22 08:09.
 */
public class EntityXmlParse extends BaseXmlParse {
	public EntityXmlParse(String basePath , String xmlConfigPath) {
		super(basePath, xmlConfigPath);
	}

	public void parseXml() {
		this.addObjectCreate("base/entity", Entity.class);
		this.addObjectCreate("base/entity/field", Field.class, "addField");
		this.addObjectCreate("base/entity/constructor", Constructor.class, "addConstructor");
	}
}
