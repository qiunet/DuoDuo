package org.qiunet.project.init.creator;

import org.apache.commons.digester.Digester;
import org.qiunet.project.init.define.IEntityDefine;
import org.qiunet.project.init.enums.EntityType;
import org.qiunet.project.init.template.VelocityFactory;
import org.qiunet.project.init.util.InitProjectUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/***
 * 创建Entity 的初始类.
 *
 * @author qiunet
 * 2019-08-16 11:56
 ***/
public class EntityCreator {
	private final Logger logger = LoggerType.DUODUO_CREATOR.getLogger();
	private final EntityType entityType;

	private final File file;

	private IEntityDefine entityDefine;

	private final String mybatisConfigPath;

	EntityCreator(EntityType entityType, File file, String mybatisConfigPath) {
		this.mybatisConfigPath = mybatisConfigPath;
		this.entityType = entityType;
		this.file = file;
	}

	void validXml(){
		try (FileInputStream fis = new FileInputStream(file)){
			entityType.validate(fis);
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

		// 输出 DoEntity
		Path outputFileName = Paths.get(entityDefine.outputPath().toString(), "entity", entityDefine.getDoName()+".java");
		VelocityFactory.getInstance().parseOutFile("vm/entity_do_create.vm", outputFileName.toString(), this.entityDefine);
		logger.info("Create Do [{}] Success!", outputFileName);

		// 输出 DoEntity
		outputFileName = Paths.get(entityDefine.outputPath().toString(), "entity", entityDefine.getBoName()+".java");
		File boFile = outputFileName.toFile();
		if (!boFile.exists()) {
			VelocityFactory.getInstance().parseOutFile("vm/entity_bo_create.vm", outputFileName.toString(), this.entityDefine);
			logger.info("Create Bo [{}] Success!", boFile);
		}

		// 输出 Service
		outputFileName = Paths.get(entityDefine.outputPath().toString(), entityDefine.getServiceName()+".java");
		File serviceFile = outputFileName.toFile();
		if (!serviceFile.exists()) {
			VelocityFactory.getInstance().parseOutFile("vm/entity_service_create.vm", outputFileName.toString(), this.entityDefine);
			logger.info("Create Service [{}] Success!", serviceFile);
		}


		// 输出 mybatis 的mapping xml
		outputFileName = Paths.get(InitProjectUtil.getRealUserDir().getAbsolutePath(), mybatisConfigPath, entityDefine.getNameSpace()+".xml");
		VelocityFactory.getInstance().parseOutFile("vm/mybatis_mapping_create.vm", outputFileName.toString(), this.entityDefine);
		logger.info("Create mapping xml [{}] Success!", outputFileName);
	}

	public IEntityDefine getEntityDefine() {
		return entityDefine;
	}

	public void setEntityDefine(IEntityDefine entityDefine) {
		this.entityDefine = entityDefine;
	}
}
