package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.base.INestListConfig;
import org.qiunet.cfg.manager.xd.XdInfoData;
import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.collection.safe.SafeList;

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
	}

	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, List<Cfg>> getNestListCfg() throws Exception{
		SafeHashMap<ID, List<Cfg>> cfgMap = new SafeHashMap<>();
		cfgs.forEach(cfg -> {
			List<Cfg> subList = cfgMap.computeIfAbsent(cfg.getId(), key -> new SafeList<>());
			subList.add(cfg);
		});

		cfgMap.values().forEach(list -> ((SafeList<Cfg>) list).safeLock());
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}

	public List<Cfg> getCfgListById(ID id) {
		return cfgMap.get(id);
	}

	public Map<ID, List<Cfg>> getCfgs() {
		return cfgMap;
	}
}
