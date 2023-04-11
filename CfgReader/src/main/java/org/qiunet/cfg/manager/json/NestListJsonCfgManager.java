package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.base.ISortable;
import org.qiunet.cfg.manager.base.ICfgWrapper;
import org.qiunet.cfg.manager.base.INestListCfgWrapper;
import org.qiunet.utils.collection.safe.SafeList;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 16:06.
 * To change this template use File | Settings | File Templates.
 */
public class NestListJsonCfgManager <ID, Cfg extends INestListCfg<ID>>
	extends BaseJsonCfgManager<ID, Cfg> implements INestListCfgWrapper<ID, Cfg> {
	private Map<ID, List<Cfg>> cfgMap;

	public NestListJsonCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	protected void loadCfg0(ICfgWrapper<ID, Cfg> wrapper) {
		this.cfgMap = ((INestListCfgWrapper<ID, Cfg>) wrapper).allCfgs();
	}

	@Override
	protected ICfgWrapper<ID, Cfg> buildWrapper(List<Cfg> cfgList) {
		SafeMap<ID, List<Cfg>> cfgMap = new SafeMap<>();
		for (Cfg cfg : cfgList) {
			List<Cfg> subList = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeList<>());
			subList.add(cfg);
		}
		for (List<Cfg> cl : cfgMap.values()) {
			if (ISortable.class.isAssignableFrom(getCfgClass())) {
				Collections.sort(((List<? extends Comparable>) cl));
			}

			((SafeList) cl).convertToUnmodifiable();
		}
		cfgMap.convertToUnmodifiable();
		cfgMap.loggerIfAbsent();
		return new INestListCfgWrapper<>() {
			@Override
			public Map<ID, List<Cfg>> allCfgs() {
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
	public Map<ID, List<Cfg>> allCfgs() {
		return cfgMap;
	}
}
