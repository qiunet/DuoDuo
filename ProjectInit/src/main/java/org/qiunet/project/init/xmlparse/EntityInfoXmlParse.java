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

	 * @param basePath	对于xmlfile和 vmfile的一个基础路径, 之后生成文件 和 vm是基于该路径的相对路径.
	 * @param xmlConfigPath xml路径
	xml 第一个元素的名称
	 */
	public EntityInfoXmlParse(String basePath, String xmlConfigPath) {
		super(EntityInfoVmElement.class, basePath, xmlConfigPath);
	}
	@Override
	public void parseXml() {
		addObjectCreate("base/beans/bean", Bean.class, "addBean");
		addObjectCreate("base/rediskey", ElementRedisKey.class, "setRedisKey");
		addObjectCreate("base/infos/info", EntityInfo.class);
	}
}
