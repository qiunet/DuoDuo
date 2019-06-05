package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INestMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class NestMapXdCfgManager<ID, SubId, Cfg extends INestMapConfig<ID, SubId>> extends BaseXdCfgManager {
	private Class<Cfg> cfgClass;

	private Map<ID, Map<SubId, Cfg>> cfgs;

	protected NestMapXdCfgManager(String fileName) {
		super(fileName);

		Type type = getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
			throw new RuntimeException("Class ["+getClass().getName()+"] 必须给定泛型!");
		}

		this.cfgClass = (Class<Cfg>) ((ParameterizedTypeImpl) type).getActualTypeArguments()[2];
		this.checkCfgClass(cfgClass);
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
		if (subIdCfgMap == null) return null;
		return subIdCfgMap.get(subId);
	}
	/***
	 * 得到一个一定格式的嵌套map
	 * 格式: key 对应 Map<subKey, cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, Map<SubId, Cfg>> getNestMapCfg() throws Exception {
		this.checkCfgClass(cfgClass);

		SafeHashMap<ID, Map<SubId, Cfg>> cfgMap = new SafeHashMap<>();
		int num = loadXdFileToDataInputStream();
		for (int i = 0; i < num; i++) {
			Cfg cfg = generalCfg(cfgClass);

			Map<SubId, Cfg> subMap = cfgMap.get(cfg.getId());
			if (subMap == null) {
				subMap = new SafeHashMap<>();
				cfgMap.put(cfg.getId(), subMap);
			}

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
