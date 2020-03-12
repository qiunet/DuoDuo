package org.qiunet.cfg.manager.xml;

import org.qiunet.cfg.base.INestMapConfig;
import org.qiunet.cfg.base.InitCfg;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Map;

/***
 *
 * @author qiunet
 * 2020-02-05 11:52
 **/
public class NestMapXmlCfgManager<ID, SubId, Cfg extends INestMapConfig<ID, SubId>> extends BaseXmlCfgManager<Cfg> {
	private Map<ID, Map<SubId, Cfg>> cfgMap;

	protected NestMapXmlCfgManager(String fileName) {
		super(fileName);
	}

	@Override
	void init() throws Exception {
		this.cfgMap = getNestMapCfg();
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

		this.cfgMap.values().stream().flatMap(val -> val.values().stream())
				.map(cfg -> (InitCfg)cfg)
				.forEach(InitCfg::init);
	}
	/***
	 * 根据id 和 subId 得到一条cfg数据
	 * @param id
	 * @param subId
	 * @return
	 */
	public Cfg getCfgByIdAndSubId(ID id, SubId subId) {
		Map<SubId, Cfg> subIdCfgMap = cfgMap.get(id);
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
		SafeMap<ID, Map<SubId, Cfg>> cfgMap = new SafeMap<>();
		for (Cfg cfg : cfgs) {
			Map<SubId, Cfg> subMap = cfgMap.computeIfAbsent(cfg.getId(), key-> new SafeMap<>());

			if (subMap.containsKey(cfg.getSubId())) {
				throw new RuntimeException("SubId ["+cfg.getSubId()+"] is duplicate!");
			}

			subMap.put(cfg.getSubId(), cfg);
		}

		for (Map<SubId, Cfg> subKeyCfgMap : cfgMap.values()) {
			((SafeMap) subKeyCfgMap).loggerIfAbsent();
			((SafeMap) subKeyCfgMap).convertToUnmodifiable();
		}

		cfgMap.loggerIfAbsent();
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}


	public Map<ID, Map<SubId, Cfg>> getCfgs() {
		return cfgMap;
	}
}
