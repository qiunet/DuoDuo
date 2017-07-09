package project.init.xmlparse;

import org.qiunet.template.creator.BaseXmlParse;
import project.init.elements.entity.Entity;
import project.init.elements.entity.Field;

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
	}
}
