package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.ISimpleMapConfig;
import org.qiunet.cfg.base.InitCfg;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Map;

/***
 * 管理 {Id -> Cfg} 类型的配置文件对象
 * @param <ID>
 * @param <Cfg>
 */
public abstract class SimpleMapXdCfgManager<ID, Cfg extends ISimpleMapConfig<ID>> extends BaseXdCfgManager<Cfg> {

	private Map<ID, Cfg> cfgMap;

	protected SimpleMapXdCfgManager(String fileName) {
		super(fileName);
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
		this.cfgMap = getSimpleMapCfg();
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

		this.cfgMap.values().stream()
				.map(cfg -> (InitCfg)cfg)
				.forEach(InitCfg::init);
	}
	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @return
	 * @throws Exception
	 */
	private Map<ID, Cfg> getSimpleMapCfg() throws Exception{
		XdInfoData xdInfoData = loadXdFileToDataInputStream();
		SafeMap<ID, Cfg> cfgMap = new SafeMap<>();
		for (int i = 0 ; i < xdInfoData.getNum(); i++ ) {
			Cfg cfg = generalCfg();

			if (cfgMap.containsKey(cfg.getId())) {
				throw new RuntimeException("ID ["+cfg.getId()+"] is duplicate!");
			}
			cfgMap.put(cfg.getId(), cfg);
		}
		cfgMap.loggerIfAbsent();
		cfgMap.convertSafe();
		return cfgMap;
	}

	public Map<ID, Cfg> getCfgs() {
		return cfgMap;
	}
}
