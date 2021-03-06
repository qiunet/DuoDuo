package org.qiunet.cfg.manager.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.convert.CfgFieldObjConvertManager;
import org.qiunet.cfg.convert.xml.XmlEnumConvert;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 *
 * @author qiunet
 * 2020-02-04 19:44
 **/
abstract class BaseXmlCfgManager<Cfg extends ICfg> extends BaseCfgManager<Cfg> {
	protected final Logger logger = LoggerType.DUODUO_CFG_READER.getLogger();
	protected List<Cfg> cfgs;

	protected BaseXmlCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	public void loadCfg() throws Exception {
		XStream xStream = new XStream();
		// allow some basics
		xStream.addPermission(NullPermission.NULL);
		xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xStream.allowTypeHierarchy(Collection.class);

		xStream.alias("configs", ArrayList.class);
		xStream.alias("config", cfgClass);
		CfgFieldObjConvertManager.getInstance().getConverts().forEach(convert -> xStream.registerConverter(convert, XStream.PRIORITY_VERY_HIGH - 1));
		xStream.registerConverter(new XmlEnumConvert(), XStream.PRIORITY_VERY_HIGH - 1);
		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			throw new NullPointerException("File ["+fileName+"] is not exist in classpath");
		}

		try (InputStream in = new FileInputStream(url.getPath())){
			this.cfgs = (List<Cfg>) xStream.fromXML(in);
			this.init();
			this.afterLoad();
		}
	}

	abstract void init() throws Exception;
}
