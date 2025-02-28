package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.manager.base.ICfgWrapper;
import org.qiunet.cfg.manager.base.INestMapCfgWrapper;
import org.qiunet.utils.collection.safe.SafeMap;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.Map;

/**
 * @author zhengj
 * Date: 2019/6/6.
 * Time: 16:10.
 * To change this template use File | Settings | File Templates.
 */
public class NestMapJsonCfgManager<ID, SubId, Cfg extends INestMapCfg<ID, SubId>>
	extends BaseJsonCfgManager<ID, Cfg> implements INestMapCfgWrapper<ID, SubId, Cfg> {
	private Map<ID, Map<SubId, Cfg>> cfgs;

	public NestMapJsonCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	protected void loadCfg0(ICfgWrapper<ID, Cfg> wrapper) {
		this.cfgs = ((INestMapCfgWrapper<ID, SubId, Cfg>) wrapper).allCfgs();
	}

	@Override
	protected ICfgWrapper<ID, Cfg> buildWrapper(List<Cfg> cfgList) {
		SafeMap<ID, Map<SubId, Cfg>> cfgMap = new SafeMap<>();
		for (Cfg cfg : cfgList) {
			Map<SubId, Cfg> subMap = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeMap<>());
			if (subMap.put(cfg.getSubId(), cfg) != null) {
				throw new CustomException("load cfg {} error! id:{} subId:{} duplicate", cfg.getClass().getSimpleName(), cfg.getId(), cfg.getSubId());
			}
		}
		for (Map<SubId, Cfg> subKeyCfgMap : cfgMap.values()) {
			((SafeMap) subKeyCfgMap).loggerIfAbsent();
			((SafeMap) subKeyCfgMap).convertToUnmodifiable();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return new INestMapCfgWrapper<>() {
			@Override
			public Map<ID, Map<SubId, Cfg>> allCfgs() {
				return cfgMap;
			}
			@Override
			public Class<Cfg> getCfgClass() {
				return cfgClass;
			}
			@Override
			public List<Cfg> list() {
				return cfgList;
			}
		};
	}

	@Override
	public Map<ID, Map<SubId, Cfg>> allCfgs() {
		return cfgs;
	}
}
