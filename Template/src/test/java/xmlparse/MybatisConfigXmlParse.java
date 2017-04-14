package xmlparse;

import elements.mybatisConfig.ElementMybatisConfig;
import org.qiunet.template.creator.BaseXmlParse;

/**
 * @author qiunet
 *         Created on 17/2/17 16:43.
 */
public class MybatisConfigXmlParse extends BaseXmlParse {
	
	public MybatisConfigXmlParse(String basePath, String xmlConfigPath) {
		super(basePath, xmlConfigPath);
	}

	@Override
	public void parseXml() {
		this.addObjectCreate("base/config", ElementMybatisConfig.class);
	}
}
