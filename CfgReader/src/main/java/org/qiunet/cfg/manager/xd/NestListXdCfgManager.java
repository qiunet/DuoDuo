package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INestListConfig;
import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.collection.safe.SafeList;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/***
 * 管理 {Id -> Cfg} 类型的配置文件对象
 * @param <ID>
 * @param <Cfg>
 */
public abstract class NestListXdCfgManager<ID, Cfg extends INestListConfig<ID>> extends BaseXdCfgManager<Cfg> {
	private Map<ID, List<Cfg>> cfgMap;

	protected NestListXdCfgManager(String fileName) {
		super(fileName);
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
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, List<Cfg>> getNestListCfg() throws Exception{
		XdInfoData xdInfoData = loadXdFileToDataInputStream();
		SafeHashMap<ID, List<Cfg>> cfgMap = new SafeHashMap<>();
		for (int i = 0; i < xdInfoData.getNum(); i++) {
			Cfg cfg = generalCfg();

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
