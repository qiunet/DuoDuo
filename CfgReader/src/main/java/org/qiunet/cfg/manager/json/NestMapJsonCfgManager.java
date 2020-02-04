package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INestMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengj
 * Date: 2019/6/6.
 * Time: 16:10.
 * To change this template use File | Settings | File Templates.
 */
public abstract class NestMapJsonCfgManager<ID, SubId, Cfg extends INestMapConfig<ID, SubId>> extends BaseJsonCfgManager<Cfg> {
	private Map<ID, Map<SubId, Cfg>> cfgs;

	protected NestMapJsonCfgManager(String fileName) {
		super(fileName);
	}

	@Override
	void init() throws Exception {
		this.cfgs = getNestMapCfg();
		this.initBySelf();
	}

	/***
	 * 根据id 和 subId 得到一条cfg数据
	 * @param id
	 * @param subId
	 * @return
	 */
	public Cfg getCfgByIdAndSubId(ID id, SubId subId) {
		Map<SubId, Cfg> subIdCfgMap = cfgs.get(id);
		if (subIdCfgMap == null) {
			return null;
		}
		return subIdCfgMap.get(subId);
	}

	/***
	 * 得到一个一定格式的嵌套map
	 * 格式: key 对应 Map<subKey, cfg>
	 * @return
	 * @throws Exception
	 */
	protected Map<ID, Map<SubId, Cfg>> getNestMapCfg() throws Exception {
		SafeHashMap<ID, Map<SubId, Cfg>> cfgMap = new SafeHashMap<>();
		List<Cfg> cfgs = getSimpleListCfg();
		for (Cfg cfg : cfgs) {
			Map<SubId, Cfg> subMap = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeHashMap<>());
			subMap.put(cfg.getSubId(), cfg);
		}
		for (Map<SubId, Cfg> subKeyCfgMap : cfgMap.values()) {
			((SafeHashMap) subKeyCfgMap).loggerIfAbsent();
			((SafeHashMap) subKeyCfgMap).safeLock();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}


	public Map<ID, Map<SubId, Cfg>> getCfgs() {
		return cfgs;
	}
}
