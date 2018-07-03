package org.qiunet.project.init.elements.mybatisConfig;

import org.qiunet.template.parse.xml.SubVmElement;

import java.util.List;

/**
 * @author qiunet
 *         Created on 17/2/17 16:36.
 */
public class ElementMybatisConfig extends SubVmElement<ConfigVmElement> {

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<ExtraELementConfig> getExtraConfigs(){
		return vmElement.getExtraConfigs();
	}

	@Override
	protected String getOutFilePath() {
		return path;
	}
}
