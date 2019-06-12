package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.ISimpleMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 15:45.
 * To change this template use File | Settings | File Templates.
 */
public abstract class SimpleMapJsonCfgManager <ID, Cfg extends ISimpleMapConfig<ID>> extends BaseJsonCfgManager {
	private Class<Cfg> cfgClass;

	private Map<ID, Cfg> cfgMap;

	protected SimpleMapJsonCfgManager(String fileName) {
		super(fileName);

		Type type = getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
			throw new RuntimeException("Class ["+getClass().getName()+"] 必须给定泛型!");
		}

		this.cfgClass = (Class<Cfg>) ((ParameterizedTypeImpl) type).getActualTypeArguments()[1];
		this.checkCfgClass(cfgClass);
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
		this.cfgMap = getSimpleMapCfg(cfgClass);
	}

	protected <Key, Cfg extends ISimpleMapConfig<Key>> Map<Key, Cfg> getSimpleMapCfg(Class<Cfg> cfgClass) throws Exception{
		SafeHashMap<Key, Cfg> cfgMap = new SafeHashMap<>();
		List<Cfg> cfgs = getSimpleListCfg("", cfgClass);
		for (Cfg cfg : cfgs) {
			cfgMap.put(cfg.getId(), cfg);
		}
		return cfgMap;
	}


	public Map<ID, Cfg> getCfgs() {
		return cfgMap;
	}

}
