package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface ISimpleMapCfgWrapper<ID, Cfg extends ISimpleMapCfg<ID>> extends ICfgWrapper<ID, Cfg> {
	/**
	 * 根据id获得配置对象
	 * @param id
	 * @return
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
	 * @return
	 */
	Map<ID, Cfg> allCfgs();


	@Override
	default int size(){
		return allCfgs().size();
	}

	/**
	 * 是否有该id的配置
	 * @param id
	 * @return
	 */
	default boolean contains(ID id){
		return allCfgs().containsKey(id);
	}
}
