import elements.entity.Entity;
import elements.info.EntityInfo;
import elements.mapping.ElementMapping;
import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;
import org.qiunet.utils.file.FileUtil;
import xmlparse.EntityInfoXmlParse;
import xmlparse.EntityXmlParse;
import org.junit.Test;
import org.qiunet.template.creator.TemplateCreator;
import xmlparse.MybatisConfigXmlParse;
import xmlparse.MybatisMappingXmlParse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * @author qiunet
 *         Created on 16/11/22 08:07.
 */
public class TemplateTest {
	@Test
	public void testTemplate(){
		String  basePath = System.getProperty("user.dir");
		BaseXmlParse entityParse = new EntityXmlParse(basePath,"src/test/resources/xml/entity_create.xml");
		BaseXmlParse entityInfoParse = new EntityInfoXmlParse(basePath,"src/test/resources/xml/entity_info_create.xml");
		BaseXmlParse mybatisConfigParse = new MybatisConfigXmlParse(basePath,"src/test/resources/xml/mybatis_config_create.xml");
		BaseXmlParse mybatisMappingParse = new MybatisMappingXmlParse(basePath,"src/test/resources/xml/mybatis_mapping_create.xml");
		
		Map<String, VmElement<? extends SubVmElement>> params = new HashMap<>();
		try {
			VmElement<Entity> entityVmElements = new TemplateCreator(entityParse , params).parseTemplate();
			params.put("entity", entityVmElements);
			this.mvVoToPath(entityVmElements);
			
			VmElement<EntityInfo> entityInfoVmElements = new TemplateCreator(entityInfoParse, params).parseTemplate();
			params.put("info", entityInfoVmElements);

			VmElement<EntityInfo> mybatisConfigVmElements = new TemplateCreator(mybatisConfigParse, params).parseTemplate();
			params.put("config", mybatisConfigVmElements);
			
			VmElement<ElementMapping> mybatisMappingVmElement = new TemplateCreator(mybatisMappingParse, params).parseTemplate();
			params.put("mapping", mybatisMappingVmElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 把外面的目录的vo挪到对应的目录
	 * @param entitys
	 */
	private void mvVoToPath(VmElement<Entity> entitys){
		String  basePath = System.getProperty("user.dir");
		if (! basePath.endsWith(File.separator)) basePath += File.separator;
		String voPath = basePath + "EntityVo/";
		
		String testDataPath = basePath + "TestCase/TestData.java";
		
		for (Entity entity : entitys.getSubVmElementList()) {
			FileUtil.copy(voPath + entity.getName().replaceAll("Po", "") +"Vo.java" , basePath + entitys.getBaseDir() + File.separator + entity.getOutFilePath()+ entity.getName().replaceAll("Po", "") +"Vo.java");
		}
		String testDataDestFile = basePath + entitys.getBaseDir() + File.separator + "org/qiunet/template/TestData.java";
		FileUtil.copy(testDataPath, testDataDestFile);
	}
}
