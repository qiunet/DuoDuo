package org.qiunet.project.init;

/**
 * Created by qiunet on 4/8/17.
 */
public final class ProjectInitCreator {
	private ProjectInitCreator(){}

	/***
	 * 根据模板创建玩家的对象
	 */
	public static void create() {
		create(new DefaultProjectInitConfig());
	}
	/***
	 * 根据模板创建玩家的对象
	 */
	public static void create(IProjectInitConfig config) {
		// 如果后期出现类错误, 没法编译通过, 无法生成的情况, 可以把这些类复制出去, 把basePath指向项目的目录, 也行.

//		BaseXmlParse mybatisConfigParse = new MybatisConfigXmlParse(basePath,config.getMybatisConfigXmlPath());
//		BaseXmlParse mybatisMappingParse = new MybatisMappingXmlParse(basePath,config.getMabatisMappingXmlPath());

		new ProjectInitData(config);

		try {
//			VmElement<ElementMapping> mybatisMappingVmElement = new TemplateCreator(mybatisMappingParse, params).parseTemplate();
//			params.put("mapping", mybatisMappingVmElement);
//
//			VmElement<EntityInfo> mybatisConfigVmElements = new TemplateCreator(mybatisConfigParse, params).parseTemplate();
//			params.put("config", mybatisConfigVmElements);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
