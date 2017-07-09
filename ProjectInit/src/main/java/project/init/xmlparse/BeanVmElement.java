package project.init.xmlparse;

import org.qiunet.template.parse.xml.VmElement;
import project.init.elements.info.Bean;
import project.init.elements.info.EntityInfo;

import java.util.HashMap;

/**
 * @author qiunet
 *         Created on 17/2/17 11:09.
 */
public class BeanVmElement extends VmElement<EntityInfo> {

	private HashMap<String, Bean> beanMap = new HashMap<String, Bean>();

	public Bean getBean(String key){
		return beanMap.get(key);
	}

	public void addBean(Bean bean) {
		beanMap.put(bean.getId(), bean);
	}
}
