package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface INestMapCfgWrapper<ID, SubID, Cfg extends INestMapCfg<ID, SubID>>
	extends ICfgWrapper<ID, Cfg> {
	/**
	 * 得到所有的配置
	 * @return
	 */
	Map<ID, Map<SubID, Cfg>> allCfgs();
	/**
	 * 根据id获得配置对象
	 * @param id
	 * @return
	 */
	default Cfg getCfgById(ID id, SubID subID){
		if (! contains(id, subID)) {
			LoggerType.DUODUO_CFG_READER.info("Cfg [{}] ID [{}] subId [{}] is missing!", getCfgClass().getName(), id, subID);
			return null;
		}
		Map<SubID, Cfg> subIDCfgMap = allCfgs().get(id);
		return subIDCfgMap.get(subID);
	}

	/**
	 * 返回id 对应的map
	 * @param id
	 * @return
	 */
	default Map<SubID, Cfg> getCfgsById(ID id){
		if (! contains(id)) {
			LoggerType.DUODUO_CFG_READER.info("Cfg [{}] ID [{}] is missing!",getCfgClass().getName(), id);
			return null;
		}
		return allCfgs().get(id);
	}

	/**
	 * 是否有该id的配置
	 * @param id
	 * @return
	 */
	default boolean contains(ID id){
		return allCfgs().containsKey(id);
	}

	/**
	 * 是否有该id的配置
	 * @param id
	 * @return
	 */
	default boolean contains(ID id, SubID subID){
		Map<ID, Map<SubID, Cfg>> allCfgs = allCfgs();
		if (! allCfgs.containsKey(id)) {
			return false;
		}
		return allCfgs.get(id).containsKey(subID);
	}

	@Override
	default int size(){
		return list().size();
	}
}
