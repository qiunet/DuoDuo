package org.qiunet.cfg.manager.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.convert.CfgFieldObjConvertManager;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

/***
 *
 * @author qiunet
 * 2020-02-04 19:44
 **/
abstract class BaseXmlCfgManager<Cfg extends ICfg> extends BaseCfgManager<Cfg> {
	protected final Logger logger = LoggerType.DUODUO.getLogger();
	protected List<Cfg> cfgs;

	protected BaseXmlCfgManager(String fileName) {
		super(fileName);
	}

	@Override
	public String loadCfg() {
		XStream xStream = new XStream();
		// allow some basics
		xStream.addPermission(NullPermission.NULL);
		xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xStream.allowTypeHierarchy(Collection.class);

		xStream.alias("configs", Configs.class);
		xStream.addImplicitArray(Configs.class, "config", cfgClass);
		CfgFieldObjConvertManager.getInstance().getConverts().forEach(convert -> xStream.registerConverter(convert, XStream.PRIORITY_VERY_HIGH - 1));

		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			throw new NullPointerException("File ["+fileName+"] is not exist in classpath");
		}

		String failFileName = "";
		InputStream in = null;
		try {
			if (url.getPath().contains(".jar!")) {
				//jar包里面的文件. 只能用这种加载方式. 缺点是有缓存. 不能热加载设定
				in = getClass().getClassLoader().getResourceAsStream(fileName);
			}else {
				in = new FileInputStream(url.getPath());
			}
			this.cfgs = (List<Cfg>) ((Configs) xStream.fromXML(in)).getConfig();

			this.init();
			this.initBySelf();
		} catch (Exception e) {
			logger.error("读取配置文件" + fileName + "失败 ERROR:", e);
			failFileName = this.fileName;
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return failFileName;
	}

	abstract void init() throws Exception;
}
