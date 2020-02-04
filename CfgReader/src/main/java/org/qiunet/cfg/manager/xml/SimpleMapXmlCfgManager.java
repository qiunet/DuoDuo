package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.base.ISimpleMapConfig;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2020-02-04 19:50
 **/
public class SimpleMapXmlCfgManager<ID, Cfg extends ISimpleMapConfig<ID>> extends BaseXmlCfgManager {
	private Map<ID, Cfg> cfgMap;

	protected SimpleMapXmlCfgManager(String fileName) {
		super(fileName);
	}
}
