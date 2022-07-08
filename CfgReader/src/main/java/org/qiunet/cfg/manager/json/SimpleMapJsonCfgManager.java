package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.ISimpleMapCfgManager;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Map;

/**
 * @author zhengj
 * Date: 2019/6/6.
 * Time: 15:45.
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMapJsonCfgManager <ID, Cfg extends ISimpleMapCfg<ID>>
		extends BaseJsonCfgManager<ID, Cfg>
		implements ISimpleMapCfgManager<ID, Cfg> {

	private Map<ID, Cfg> cfgMap;

	public SimpleMapJsonCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
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
		if (! INeedInitCfg.class.isAssignableFrom(getCfgClass())) {
			return;
		}

		this.cfgMap.values().stream()
				.map(cfg -> (INeedInitCfg)cfg)
				.forEach(INeedInitCfg::init);
	}

	protected Map<ID, Cfg> getSimpleMapCfg() throws Exception{
		SafeMap<ID, Cfg> cfgMap = new SafeMap<>();
		for (Cfg cfg : this.cfgList) {
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
