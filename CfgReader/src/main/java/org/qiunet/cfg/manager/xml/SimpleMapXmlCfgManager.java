package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.base.ISimpleMapConfig;
import org.qiunet.cfg.base.InitCfg;
import org.qiunet.utils.collection.safe.SafeHashMap;

import java.util.Map;

/***
 *
 * @author qiunet
 * 2020-02-04 19:50
 **/
public abstract class SimpleMapXmlCfgManager<ID, Cfg extends ISimpleMapConfig<ID>> extends BaseXmlCfgManager<Cfg> {
	private Map<ID, Cfg> cfgMap;

	protected SimpleMapXmlCfgManager(String fileName) {
		super(fileName);
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
		if (! InitCfg.class.isAssignableFrom(getCfgClass())) {
			return;
		}

		this.cfgMap.values().stream()
				.map(cfg -> (InitCfg)cfg)
				.forEach(InitCfg::init);
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, Cfg> getSimpleMapCfg() {
		SafeHashMap<ID, Cfg> cfgMap = new SafeHashMap<>();
		for(Cfg cfg : cfgs) {
			if (cfgMap.containsKey(cfg.getId())) {
				throw new RuntimeException("ID ["+cfg.getId()+"] is duplicate!");
			}
			cfgMap.put(cfg.getId(), cfg);
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}

	public Map<ID, Cfg> getCfgs() {
		return cfgMap;
	}

	public Cfg getCfgById(ID id) {
		return cfgMap.get(id);
	}
}
