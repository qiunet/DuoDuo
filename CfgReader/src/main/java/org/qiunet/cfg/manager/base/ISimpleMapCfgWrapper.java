package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;

/***
 * 一对一的map存储结构
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface ISimpleMapCfgWrapper<ID, Cfg extends ISimpleMapCfg<ID>> extends ICfgWrapper<ID, Cfg> {
	/**
	 * 根据id获得配置对象
	 * @param id 指定id
	 * @return 指定的cfg对象
	 */
	default Cfg getCfgById(ID id){
		if (! contains(id)) {
			LoggerType.DUODUO_CFG_READER.info("Cfg [{}] ID [{}] is missing!", getCfgClass().getName(), id);
			return null;
		}
		return allCfgs().get(id);
	}

	/**
	 * 得到所有的配置
	 * @return 包含所有数据的map
	 */
	Map<ID, Cfg> allCfgs();

	/**
	 * 是否有该id的配置
	 * @param id 指定id
	 * @return 是否包含
	 */
	default boolean contains(ID id){
		return allCfgs().containsKey(id);
	}
}
