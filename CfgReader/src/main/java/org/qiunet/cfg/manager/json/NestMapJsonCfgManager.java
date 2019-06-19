package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INestMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 16:10.
 * To change this template use File | Settings | File Templates.
 */
public abstract class NestMapJsonCfgManager<ID, SubId, Cfg extends INestMapConfig<ID, SubId>> extends BaseJsonCfgManager {
	private Class<Cfg> cfgClass;

	private Map<ID, Map<SubId, Cfg>> cfgs;

	protected NestMapJsonCfgManager(String fileName) {
		super(fileName);

		Type type = getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
			throw new RuntimeException("Class [" + getClass().getName() + "] 必须给定泛型!");
		}

		this.cfgClass = (Class<Cfg>) ((ParameterizedTypeImpl) type).getActualTypeArguments()[2];
		this.checkCfgClass(cfgClass);
	}

	@Override
	void init() throws Exception {
		this.cfgs = getNestMapCfg(cfgClass);
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
	 * @param cfgClass
	 * @param <Key>
	 * @param <SubKey>
	 * @param <Cfg>
	 * @return
	 * @throws Exception
	 */
	protected <Key, SubKey, Cfg extends INestMapConfig<Key, SubKey>> Map<Key, Map<SubKey, Cfg>> getNestMapCfg(Class<Cfg> cfgClass) throws Exception {
		SafeHashMap<Key, Map<SubKey, Cfg>> cfgMap = new SafeHashMap<>();
		List<Cfg> cfgs = getSimpleListCfg("", cfgClass);
		for (Cfg cfg : cfgs) {
			Map<SubKey, Cfg> subMap = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeHashMap<>());
			subMap.put(cfg.getSubId(), cfg);
		}
		for (Map<SubKey, Cfg> subKeyCfgMap : cfgMap.values()) {
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
