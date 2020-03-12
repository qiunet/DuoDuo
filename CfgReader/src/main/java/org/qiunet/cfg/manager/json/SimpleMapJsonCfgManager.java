package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.ISimpleMapConfig;
import org.qiunet.cfg.base.InitCfg;
import org.qiunet.utils.collection.safe.SafeMap;

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

	protected Map<ID, Cfg> getSimpleMapCfg() throws Exception{
		SafeMap<ID, Cfg> cfgMap = new SafeMap<>();
		List<Cfg> cfgs = getSimpleListCfg();
		for (Cfg cfg : cfgs) {
			cfgMap.put(cfg.getId(), cfg);
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}


	public Map<ID, Cfg> getCfgs() {
		return cfgMap;
	}

}
