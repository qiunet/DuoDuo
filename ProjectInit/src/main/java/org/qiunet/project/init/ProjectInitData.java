package org.qiunet.project.init;

import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.info.EntityInfo;
import org.qiunet.project.init.elements.mapping.ElementMapping;
import org.qiunet.project.init.elements.mybatisConfig.ElementMybatisConfig;
import org.qiunet.project.init.xmlparse.EntityXmlParse;
import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.template.creator.TemplateCreator;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/29 17:41
 **/
public class ProjectInitData {
	private IProjectInitConfig config;
	private VmElement<Entity> entity;
	private VmElement<EntityInfo> entityInfo;
	private VmElement<ElementMapping> elementMapping;
	private VmElement<ElementMybatisConfig> elementMybatisConfig;


	private SubVmElement currData;

	public ProjectInitData(IProjectInitConfig config) {
		this.config = config;

		this.initEntity();
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
		BaseXmlParse entityParse = new EntityXmlParse(config.getBasePath(), config.getEntityXmlPath());
		this.entity = new TemplateCreator(entityParse, this).parseTemplate();
	}

	public SubVmElement getCurrData() {
		return currData;
	}

	public IProjectInitConfig getConfig() {
		return config;
	}

	public void setCurrData(SubVmElement currData) {
		this.currData = currData;
	}
}
