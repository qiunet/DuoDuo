package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.ISimpleMapCfgManager;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Map;

/***
 * 管理 {Id -> Cfg} 类型的配置文件对象
 * @param <ID>
 * @param <Cfg>
 */
public class SimpleMapXdCfgManager<ID, Cfg extends ISimpleMapCfg<ID>> extends BaseXdCfgManager<Cfg> implements ISimpleMapCfgManager<ID, Cfg> {

	private Map<ID, Cfg> cfgMap;

	public SimpleMapXdCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
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
		if (! INeedInitCfg.class.isAssignableFrom(getCfgClass())) {
			return;
		}

		this.cfgMap.values().stream()
				.map(cfg -> (INeedInitCfg)cfg)
				.forEach(INeedInitCfg::init);
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
		cfgMap.convertToUnmodifiable();
		return cfgMap;
	}

	@Override
	public Map<ID, Cfg> allCfgs() {
		return cfgMap;
	}
}
