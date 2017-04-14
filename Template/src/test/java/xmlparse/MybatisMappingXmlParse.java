package xmlparse;

import elements.mapping.ElementMapping;
import elements.mybatisConfig.ElementMybatisConfig;
import org.qiunet.template.creator.BaseXmlParse;

/**
 * @author qiunet
 *         Created on 17/2/23 18:15.
 */
public class MybatisMappingXmlParse extends BaseXmlParse {
	public MybatisMappingXmlParse(String basePath, String xmlConfigPath) {
		super(basePath, xmlConfigPath);
	}

	@Override
	public void parseXml() {
		this.addObjectCreate("base/mapping", ElementMapping.class);
	}
}
