package org.qiunet.project.init;

import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.info.EntityInfo;
import org.qiunet.project.init.elements.mapping.ElementMapping;
import org.qiunet.project.init.elements.mybatisConfig.ElementMybatisConfig;
import org.qiunet.template.parse.xml.VmElement;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/29 17:41
 **/
public class InitData {
	private IProjectInitConfig config;
	private VmElement<Entity> entity;
	private VmElement<EntityInfo> entityInfo;
	private VmElement<ElementMapping> elementMapping;
	private VmElement<ElementMybatisConfig> elementMybatisConfig;

	public InitData(IProjectInitConfig config) {
		this.config = config;
	}

	public VmElement<Entity> getEntity() {
		return entity;
	}

	public void setEntity(VmElement<Entity> entity) {
		this.entity = entity;
	}

	public VmElement<EntityInfo> getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(VmElement<EntityInfo> entityInfo) {
		this.entityInfo = entityInfo;
	}

	public VmElement<ElementMapping> getElementMapping() {
		return elementMapping;
	}

	public void setElementMapping(VmElement<ElementMapping> elementMapping) {
		this.elementMapping = elementMapping;
	}

	public VmElement<ElementMybatisConfig> getElementMybatisConfig() {
		return elementMybatisConfig;
	}

	public void setElementMybatisConfig(VmElement<ElementMybatisConfig> elementMybatisConfig) {
		this.elementMybatisConfig = elementMybatisConfig;
	}
}
