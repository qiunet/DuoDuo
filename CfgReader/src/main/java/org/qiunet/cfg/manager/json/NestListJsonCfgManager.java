package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INestListConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.collection.safe.SafeList;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 16:06.
 * To change this template use File | Settings | File Templates.
 */
public abstract class NestListJsonCfgManager <ID, Cfg extends INestListConfig<ID>> extends BaseJsonCfgManager {
	private Class<Cfg> cfgClass;

	private Map<ID, List<Cfg>> cfgMap;

	protected NestListJsonCfgManager(String fileName) {
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
	public List<Cfg> getCfgListById(ID id) {
		return cfgMap.get(id);
	}

	@Override
	void init() throws Exception {
		this.cfgMap = getNestListCfg(cfgClass);
	}

	/***
	 * 得到嵌套list的map数据
	 * 一个key  对应一个 cfg list的结构
	 * @param cfgClass
	 * @param <Key>
	 * @param <Cfg>
	 * @return
	 * @throws Exception
	 */
	protected <Key, Cfg extends INestListConfig<Key>> Map<Key, List<Cfg>> getNestListCfg(Class<Cfg> cfgClass) throws Exception {
		SafeHashMap<Key, List<Cfg>> cfgMap = new SafeHashMap<>();
		List<Cfg> cfgs = getSimpleListCfg("", cfgClass);
		for (Cfg cfg : cfgs) {
			List<Cfg> subList = cfgMap.get(cfg.getId());
			if (subList == null) {
				subList = new SafeList<>();
				cfgMap.put(cfg.getId(), subList);
			}
			subList.add(cfg);
		}
		for (List<Cfg> cfgList : cfgMap.values()) {
			((SafeList) cfgList).safeLock();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}


	public Map<ID, List<Cfg>> getCfgs() {
		return cfgMap;
	}
}
