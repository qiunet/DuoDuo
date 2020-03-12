package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.base.INestListConfig;
import org.qiunet.cfg.base.InitCfg;
import org.qiunet.utils.collection.safe.SafeList;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2020-02-05 11:41
 **/
public class NestListXmlCfgManager<ID, Cfg extends INestListConfig<ID>> extends BaseXmlCfgManager<Cfg> {
	private Map<ID, List<Cfg>> cfgMap;

	protected NestListXmlCfgManager(String fileName) {
		super(fileName);
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
		if (! InitCfg.class.isAssignableFrom(getCfgClass())) {
			return;
		}

		this.cfgMap.values().stream().flatMap(Collection::stream)
				.map(cfg -> (InitCfg)cfg)
				.forEach(InitCfg::init);
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, List<Cfg>> getNestListCfg() throws Exception{
		SafeMap<ID, List<Cfg>> cfgMap = new SafeMap<>();
		cfgs.forEach(cfg -> {
			List<Cfg> subList = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeList<>());
			subList.add(cfg);
		});

		cfgMap.values().forEach(list -> ((SafeList<Cfg>) list).convertToUnmodifiable());
		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}

	public List<Cfg> getCfgListById(ID id) {
		return cfgMap.get(id);
	}

	public Map<ID, List<Cfg>> getCfgs() {
		return cfgMap;
	}
}
