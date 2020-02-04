package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/***
 *
 * @author qiunet
 * 2020-02-04 19:44
 **/
abstract class BaseXmlCfgManager<cfg extends ICfg> extends BaseCfgManager<cfg> {
	protected final Logger logger = LoggerType.DUODUO.getLogger();
	protected String fileName;

	protected BaseXmlCfgManager(String fileName) {
		super(fileName);

		Cfg annotation = getClass().getAnnotation(Cfg.class);
		CfgManagers.getInstance().addDataSettingManager(this, annotation == null? 0: annotation.order());
		this.fileName = fileName;
	}

	@Override
	public String loadCfg() throws Exception {
		return null;
	}
}
