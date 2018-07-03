package org.qiunet.project.init;

import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.entity.EntityVmElement;
import org.qiunet.project.init.elements.info.EntityInfo;
import org.qiunet.project.init.elements.info.EntityInfoVmElement;
import org.qiunet.project.init.elements.info.EntityVo;
import org.qiunet.project.init.elements.mapping.ElementMapping;
import org.qiunet.project.init.elements.mapping.MappingVmElement;
import org.qiunet.project.init.elements.mybatisConfig.ConfigVmElement;
import org.qiunet.project.init.elements.mybatisConfig.ElementMybatisConfig;
import org.qiunet.project.init.xmlparse.EntityInfoXmlParse;
import org.qiunet.project.init.xmlparse.EntityXmlParse;
import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.template.creator.TemplateCreator;
import org.qiunet.template.parse.template.VelocityFactory;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;

import java.io.File;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/29 17:41
 **/
public class ProjectInitData {
	private IProjectInitConfig config;
	private EntityVmElement entity;
	private EntityInfoVmElement entityInfo;
	private MappingVmElement elementMapping;
	private ConfigVmElement elementMybatisConfig;


	private Object currData;

	public ProjectInitData(IProjectInitConfig config) {
		this.config = config;

		this.initEntity();
		this.initEntityInfo();
		this.createEntityVo();
	}
	/***
	 * 得到某个po的Entity对象
	 * @param poName
	 * @return
	 */
	public Entity getEntity(String poName) {
		return entity.subVmElement(poName);
	}

	private void initEntity() {
		EntityXmlParse entityParse = new EntityXmlParse(config.getBasePath(), config.getEntityXmlPath());
		TemplateCreator<EntityVmElement, EntityXmlParse> creator = new TemplateCreator(entityParse, this);
		this.entity = creator.parseTemplate();
	}

	private void initEntityInfo(){
		EntityInfoXmlParse entityInfoParse = new EntityInfoXmlParse(config.getBasePath(),config.getEntityInfoXmlPath());
		TemplateCreator<EntityInfoVmElement, EntityInfoXmlParse> creator = new TemplateCreator(entityInfoParse, this);
		this.entityInfo = creator.parseTemplate();
	}

	public Object getCurrData() {
		return currData;
	}

	public IProjectInitConfig getConfig() {
		return config;
	}

	public void setCurrData(Object currData) {
		this.currData = currData;
	}

	/***
	 * 生成vo
	 */
	private void createEntityVo() {
		String basePath = config.getBasePath();
		StringBuilder poBasePath = new StringBuilder(basePath);
		if (! basePath.endsWith(File.separator)) poBasePath.append(File.separator);
		poBasePath.append(entity.getBaseDir());
		if (! entity.getBaseDir().endsWith(File.separator)) poBasePath.append(File.separator);

		for (EntityInfo info : entityInfo.getSubVmElementList()) {
			if (! info.getVo().equals(info.getPoref())) {
				StringBuilder sb = new StringBuilder();
				sb.append(poBasePath).append(getEntity(info.getPoref()).getOutFilePath()).append(info.getVo()).append(".java");

				File file = new File(sb.toString());
				if (! file.exists()) {
					EntityVo entityVo = new EntityVo(getEntity(info.getPoref()), info.getVo());
					this.currData = entityVo;

					VelocityFactory.getInstance().parseOutFile("vm/entity_vo_create.vm", sb.toString(), this);
				}
			}
		}
	}
}
