package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.base.ISortable;
import org.qiunet.cfg.manager.base.ICfgWrapper;
import org.qiunet.cfg.manager.base.ISimpleMapCfgWrapper;
import org.qiunet.utils.collection.safe.SafeMap;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zhengj
 * Date: 2019/6/6.
 * Time: 15:45.
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMapJsonCfgManager <ID, Cfg extends ISimpleMapCfg<ID>>
		extends BaseJsonCfgManager<ID, Cfg> implements ISimpleMapCfgWrapper<ID, Cfg> {

	private Map<ID, Cfg> cfgMap;

	public SimpleMapJsonCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	protected void loadCfg0(ICfgWrapper<ID, Cfg> wrapper) {
		this.cfgMap = ((ISimpleMapCfgWrapper<ID, Cfg>) wrapper).allCfgs();
	}

	@Override
	protected ICfgWrapper<ID, Cfg> buildWrapper(List<Cfg> cfgList) {
		SafeMap<ID, Cfg> cfgMap = new SafeMap<>();

		if (ISortable.class.isAssignableFrom(getCfgClass())) {
			Collections.sort(((List<? extends Comparable>) cfgList));
		}

		for (Cfg cfg : cfgList) {
			if(cfgMap.put(cfg.getId(), cfg) != null) {
				throw new CustomException("load cfg {} error! id:{} duplicate", cfg.getClass().getSimpleName(), cfg.getId());
			}
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return new ISimpleMapCfgWrapper<>() {
			@Override
			public Map<ID, Cfg> allCfgs() {
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
	public Map<ID, Cfg> allCfgs() {
		return cfgMap;
	}

}
