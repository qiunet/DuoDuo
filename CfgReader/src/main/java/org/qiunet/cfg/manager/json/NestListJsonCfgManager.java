package org.qiunet.cfg.manager.json;

import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.manager.base.INestListCfgManager;
import org.qiunet.utils.collection.safe.SafeList;
import org.qiunet.utils.collection.safe.SafeMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 16:06.
 * To change this template use File | Settings | File Templates.
 */
public class NestListJsonCfgManager <ID, Cfg extends INestListCfg<ID>>
	extends BaseJsonCfgManager<ID, Cfg> implements INestListCfgManager<ID, Cfg> {
	private Map<ID, List<Cfg>> cfgMap;

	public NestListJsonCfgManager(Class<Cfg> cfgClass) {
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
	 * 得到嵌套list的map数据
	 * 一个key  对应一个 cfg list的结构
	 * @return
	 * @throws Exception
	 */
	protected Map<ID, List<Cfg>> getNestListCfg() throws Exception {
		SafeMap<ID, List<Cfg>> cfgMap = new SafeMap<>();
		for (Cfg cfg : this.cfgList) {
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
