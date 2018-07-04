package org.qiunet.project.init;

import org.qiunet.project.init.elements.entity.Entity;
import org.qiunet.project.init.elements.entity.EntityVmElement;
import org.qiunet.project.init.elements.info.EntityInfo;
import org.qiunet.project.init.elements.info.EntityInfoVmElement;
import org.qiunet.project.init.elements.info.EntityService;
import org.qiunet.project.init.elements.info.EntityVo;
import org.qiunet.project.init.elements.mapping.ElementMapping;
import org.qiunet.project.init.elements.mapping.MappingVmElement;
import org.qiunet.project.init.elements.mybatisConfig.ConfigVmElement;
import org.qiunet.project.init.xmlparse.EntityInfoXmlParse;
import org.qiunet.project.init.xmlparse.EntityXmlParse;
import org.qiunet.project.init.xmlparse.MybatisConfigXmlParse;
import org.qiunet.project.init.xmlparse.MybatisMappingXmlParse;
import org.qiunet.template.creator.TemplateCreator;
import org.qiunet.template.parse.template.VelocityFactory;

import java.io.File;
import java.util.List;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/29 17:41
 **/
public class ProjectInitData {
	private IProjectInitConfig config;
	private EntityVmElement entity;
	private EntityInfoVmElement entityInfo;
	private ConfigVmElement elementConfig;
	private MappingVmElement elementMapping;


	private Object currData;

	public ProjectInitData(IProjectInitConfig config) {
		this.config = config;
	}

	public void create(){
		this.initEntity();
		this.initEntityInfo();
		this.createEntityVo();
		this.createService();
		this.initMybatisMapping();
		this.initMybatisConfig();
	}
	/***
	 * 得到某个po的Entity对象
	 * @param poName
	 * @return
	 */
	public Entity getEntity(String poName) {
		return entity.subVmElement(poName);
	}

	/***
	 * 得到所有的entity list
	 * @return
	 */
	public List<Entity> getAllEntity() {
		return entity.getSubVmElementList();
	}

	/***
	 * 得到所有的entity list
	 * @return
	 */
	public List<ElementMapping> getAllElementMapping() {
		return elementMapping.getSubVmElementList();
	}

	private void initEntity() {
		EntityXmlParse entityParse = new EntityXmlParse( config.getEntityXmlPath());
		TemplateCreator<EntityVmElement> creator = new TemplateCreator(entityParse, this);
		this.entity = creator.parseTemplate();
	}

	private void initEntityInfo(){
		EntityInfoXmlParse entityInfoParse = new EntityInfoXmlParse(config.getEntityInfoXmlPath());
		TemplateCreator<EntityInfoVmElement> creator = new TemplateCreator(entityInfoParse, this);
		this.entityInfo = creator.parseTemplate();
	}

	private void initMybatisConfig(){
		MybatisConfigXmlParse mybatisConfigXmlParse = new MybatisConfigXmlParse( config.getMybatisConfigXmlPath());
		TemplateCreator<ConfigVmElement> creator = new TemplateCreator(mybatisConfigXmlParse, this);
		this.elementConfig = creator.parseTemplate();
	}

	private void initMybatisMapping(){
		MybatisMappingXmlParse mybatisMappingParse = new MybatisMappingXmlParse( config.getMabatisMappingXmlPath());
		TemplateCreator<MappingVmElement> creator = new TemplateCreator(mybatisMappingParse, this);
		this.elementMapping = creator.parseTemplate();
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

	/***
	 * 生成service对象
	 */
	private void createService(){
		String basePath = config.getBasePath();
		StringBuilder poBasePath = new StringBuilder(basePath);
		if (! basePath.endsWith(File.separator)) poBasePath.append(File.separator);
		poBasePath.append(entity.getBaseDir());
		if (! entity.getBaseDir().endsWith(File.separator)) poBasePath.append(File.separator);

		for (EntityInfo info : entityInfo.getSubVmElementList()) {
			StringBuilder sb = new StringBuilder();
			String servicePath = info.getServicePackagePath();
			sb.append(poBasePath).append(servicePath.replace(".", File.separator)).append(File.separator).append(info.getServiceFileName()).append(".java");

			File file = new File(sb.toString());
			if (! file.exists()) {
				this.currData = new EntityService(info.getServiceFileName(), getEntity(info.getPoref()), info, servicePath);
				VelocityFactory.getInstance().parseOutFile("vm/entity_service_create.vm", sb.toString(), this);
			}
		}
	}
}
