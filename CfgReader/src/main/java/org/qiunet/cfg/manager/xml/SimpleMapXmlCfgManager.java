package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.ISimpleMapCfgManager;
import org.qiunet.utils.collection.safe.SafeMap;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;

/***
 *
 * @author qiunet
 * 2020-02-04 19:50
 **/
public class SimpleMapXmlCfgManager<ID, Cfg extends ISimpleMapCfg<ID>> extends BaseXmlCfgManager<Cfg> implements ISimpleMapCfgManager<ID, Cfg> {
	private Map<ID, Cfg> cfgMap;

	public SimpleMapXmlCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	void init() throws Exception{
		this.cfgMap = getSimpleMapCfg();
		this.initCfgSelf();
	}
	/***
	 * 如果cfg 对象是实现了 initCfg接口,
	 * 就调用init方法实现cfg的二次init.
	 */
	private void initCfgSelf() {
		if (! INeedInitCfg.class.isAssignableFrom(getCfgClass())) {
			return;
		}

		this.cfgMap.values().stream()
				.map(cfg -> (INeedInitCfg)cfg)
				.forEach(INeedInitCfg::init);
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, Cfg> getSimpleMapCfg() {
		SafeMap<ID, Cfg> cfgMap = new SafeMap<>();
		for(Cfg cfg : cfgs) {
			if (cfgMap.containsKey(cfg.getId())) {
				throw new CustomException("ID [{}] is duplicate!", cfg.getId());
			}
			cfgMap.put(cfg.getId(), cfg);
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}

	@Override
	public Map<ID, Cfg> allCfgs() {
		return cfgMap;
	}
}
