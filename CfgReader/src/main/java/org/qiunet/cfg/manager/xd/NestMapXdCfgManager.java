package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.manager.base.INestMapCfgManager;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Map;

/**
 *
 * @param <ID>
 * @param <SubId>
 * @param <Cfg>
 */
public class NestMapXdCfgManager<ID, SubId, Cfg extends INestMapCfg<ID, SubId>>
	extends BaseXdCfgManager<Cfg> implements INestMapCfgManager<ID, SubId, Cfg> {
	private Map<ID, Map<SubId, Cfg>> cfgs;

	public NestMapXdCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	void init() throws Exception {
		this.cfgs = getNestMapCfg();
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

		this.cfgs.values().stream().flatMap(val -> val.values().stream())
				.map(cfg -> (INeedInitCfg)cfg)
				.forEach(INeedInitCfg::init);
	}

	/***
	 * 得到一个一定格式的嵌套map
	 * 格式: key 对应 Map<subKey, cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, Map<SubId, Cfg>> getNestMapCfg() throws Exception {
		SafeMap<ID, Map<SubId, Cfg>> cfgMap = new SafeMap<>();
		XdInfoData xdInfoData = loadXdFileToDataInputStream();
		for (int i = 0; i < xdInfoData.getNum(); i++) {
			Cfg cfg = generalCfg();
			Map<SubId, Cfg> subMap = cfgMap.computeIfAbsent(cfg.getId(), key-> new SafeMap<>());

			if (subMap.containsKey(cfg.getSubId())) {
				throw new RuntimeException("SubId ["+cfg.getSubId()+"] is duplicate!");
			}

			subMap.put(cfg.getSubId(), cfg);
		}
		for (Map<SubId, Cfg> subKeyCfgMap : cfgMap.values()) {
			((SafeMap) subKeyCfgMap).loggerIfAbsent();
			((SafeMap) subKeyCfgMap).convertToUnmodifiable();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}

	@Override
	public Map<ID, Map<SubId, Cfg>> allCfgs() {
		return cfgs;
	}
}
