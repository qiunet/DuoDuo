package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.ISimpleMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;

import java.util.List;
import java.util.Map;

/**
 * @author zhengj
 * Date: 2019/6/6.
 * Time: 15:45.
 * To change this template use File | Settings | File Templates.
 */
public abstract class SimpleMapJsonCfgManager <ID, Cfg extends ISimpleMapConfig<ID>> extends BaseJsonCfgManager<Cfg> {
	private Map<ID, Cfg> cfgMap;

	protected SimpleMapJsonCfgManager(String fileName) {
		super(fileName);
	}

	/**
	 * 根据id得到对应的Cfg
	 * @param id
	 * @return
	 */
	public Cfg getCfgById(ID id) {
		return cfgMap.get(id);
	}

	@Override
	void init() throws Exception {
		this.cfgMap = getSimpleMapCfg();
	}

	protected Map<ID, Cfg> getSimpleMapCfg() throws Exception{
		SafeHashMap<ID, Cfg> cfgMap = new SafeHashMap<>();
		List<Cfg> cfgs = getSimpleListCfg();
		for (Cfg cfg : cfgs) {
			cfgMap.put(cfg.getId(), cfg);
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}


	public Map<ID, Cfg> getCfgs() {
		return cfgMap;
	}

}
