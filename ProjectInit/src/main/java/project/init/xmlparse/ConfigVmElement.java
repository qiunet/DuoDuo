package project.init.xmlparse;

import org.qiunet.template.parse.xml.VmElement;
import project.init.elements.mybatisConfig.ElementMybatisConfig;
import project.init.elements.mybatisConfig.ExtraELementConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiunet.
 * 17/7/9
 */
public class ConfigVmElement extends VmElement<ElementMybatisConfig> {

	private List<ExtraELementConfig> extraConfigs;

	public ConfigVmElement(){
		extraConfigs = new ArrayList<>();
	}

	public List<ExtraELementConfig> getExtraConfigs() {
		return extraConfigs;
	}

	public void addExtraConfig(ExtraELementConfig config) {
		this.extraConfigs.add(config);
	}
}
