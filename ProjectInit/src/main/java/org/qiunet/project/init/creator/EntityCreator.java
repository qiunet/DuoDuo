package org.qiunet.project.init.creator;

import org.apache.commons.digester.Digester;
import org.qiunet.project.init.define.IEntityDefine;
import org.qiunet.project.init.enums.EntityType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/***
 *
 *
 * qiunet
 * 2019-08-16 11:56
 ***/
public class EntityCreator {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private EntityType entityType;

	private File file;

	private IEntityDefine entityDefine;

	EntityCreator(EntityType entityType, File file) {
		this.entityType = entityType;
		this.file = file;
	}

	void validXml(){
		try {
			entityType.validate(new FileInputStream(file));
		}catch (Exception e) {
			logger.error("file ["+file.getName()+"] create exception:", e);
		}
	}

	void parse(){
		this.validXml();

		Digester digester = new Digester();
		digester.push(this);
		entityType.initDigester(digester);
		try {
			digester.parse(file);
		} catch (IOException | SAXException e) {
			logger.error("Digester parse error:" , e);
		}
	}


	public void setEntityDefine(IEntityDefine entityDefine) {
		this.entityDefine = entityDefine;
	}
}
