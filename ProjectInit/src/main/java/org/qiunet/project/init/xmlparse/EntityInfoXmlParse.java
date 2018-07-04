package org.qiunet.project.init.xmlparse;

import org.qiunet.project.init.elements.info.EntityInfoVmElement;
import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.project.init.elements.info.Bean;
import org.qiunet.project.init.elements.info.ElementRedisKey;
import org.qiunet.project.init.elements.info.EntityInfo;

/**
 * @author qiunet
 *         Created on 17/2/15 18:20.
 */
public class EntityInfoXmlParse extends BaseXmlParse<EntityInfoVmElement> {

	/***
	 * 构造一个 xmlparse 解析 xml

	 * @param xmlConfigPath xml路径
	xml 第一个元素的名称
	 */
	public EntityInfoXmlParse(String xmlConfigPath) {
		super(EntityInfoVmElement.class, xmlConfigPath);
	}
	@Override
	public void parseXml() {
		addObjectCreate("base/beans/bean", Bean.class, "addBean");
		addObjectCreate("base/rediskey", ElementRedisKey.class, "setRedisKey");
		addObjectCreate("base/infos/info", EntityInfo.class);
	}
}
