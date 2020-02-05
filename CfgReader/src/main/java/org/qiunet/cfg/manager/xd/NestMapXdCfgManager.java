package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INestMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import java.util.Map;

/**
 *
 * @param <ID>
 * @param <SubId>
 * @param <Cfg>
 */
public abstract class NestMapXdCfgManager<ID, SubId, Cfg extends INestMapConfig<ID, SubId>> extends BaseXdCfgManager<Cfg> {
	private Map<ID, Map<SubId, Cfg>> cfgs;

	protected NestMapXdCfgManager(String fileName) {
		super(fileName);
	}

	@Override
	void init() throws Exception {
		this.cfgs = getNestMapCfg();
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
	private Map<ID, Map<SubId, Cfg>> getNestMapCfg() throws Exception {
		SafeHashMap<ID, Map<SubId, Cfg>> cfgMap = new SafeHashMap<>();
		XdInfoData xdInfoData = loadXdFileToDataInputStream();
		for (int i = 0; i < xdInfoData.getNum(); i++) {
			Cfg cfg = generalCfg();
			Map<SubId, Cfg> subMap = cfgMap.computeIfAbsent(cfg.getId(), key-> new SafeHashMap<>());

			if (subMap.containsKey(cfg.getSubId())) {
				throw new RuntimeException("SubId ["+cfg.getSubId()+"] is duplicate!");
			}

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
