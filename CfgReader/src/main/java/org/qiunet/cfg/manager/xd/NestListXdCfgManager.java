package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INestListConfig;
import org.qiunet.cfg.base.ISimpleMapConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.collection.safe.SafeList;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/***
 * 管理 {Id -> Cfg} 类型的配置文件对象
 * @param <ID>
 * @param <Cfg>
 */
public abstract class NestListXdCfgManager<ID, Cfg extends INestListConfig<ID>> extends BaseXdCfgManager {

	private Class<Cfg> cfgClass;

	private Map<ID, List<Cfg>> cfgMap;

	protected NestListXdCfgManager(String fileName) {
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
		this.cfgMap = getNestListCfg();
		this.initBySelf();
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, List<Cfg>> getNestListCfg() throws Exception{
		int num = loadXdFileToDataInputStream();
		SafeHashMap<ID, List<Cfg>> cfgMap = new SafeHashMap<>();
		for (int i = 0; i < num; i++) {
			Cfg cfg = generalCfg(cfgClass);

			List<Cfg> subList = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeList<>());
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
