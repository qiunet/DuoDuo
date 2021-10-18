package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.manager.base.INestListCfgManager;
import org.qiunet.utils.collection.safe.SafeList;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/***
 * 管理 {Id to Cfg} 类型的配置文件对象
 * @param <ID>
 * @param <Cfg>
 */
public class NestListXdCfgManager<ID, Cfg extends INestListCfg<ID>>
	extends BaseXdCfgManager<Cfg> implements INestListCfgManager<ID, Cfg> {
	private Map<ID, List<Cfg>> cfgMap;

	public NestListXdCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	void init() throws Exception {
		this.cfgMap = getNestListCfg();
		this.initCfgSelf();
	}
	/***
	 * 如果cfg 对象是实现了 initCfg接口,
	 * 就调用init方法实现cfg的二次init.
	 */
	private void initCfgSelf() {
		if (! INeedInitCfg.class.isAssignableFrom(getCfgClass())) {
			return;
		}

		this.cfgMap.values().stream().flatMap(Collection::stream)
				.map(cfg -> (INeedInitCfg)cfg)
				.forEach(INeedInitCfg::init);
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, List<Cfg>> getNestListCfg() throws Exception{
		XdInfoData xdInfoData = loadXdFileToDataInputStream();
		SafeMap<ID, List<Cfg>> cfgMap = new SafeMap<>();
		for (int i = 0; i < xdInfoData.getNum(); i++) {
			Cfg cfg = generalCfg();

			List<Cfg> subList = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeList<>());
			subList.add(cfg);
		}
		for (List<Cfg> cfgList : cfgMap.values()) {
			((SafeList) cfgList).convertToUnmodifiable();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}

	@Override
	public Map<ID, List<Cfg>> allCfgs() {
		return cfgMap;
	}
}
