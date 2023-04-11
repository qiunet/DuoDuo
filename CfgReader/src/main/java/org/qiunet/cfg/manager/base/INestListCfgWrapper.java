package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.utils.logger.LoggerType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface INestListCfgWrapper<ID, Cfg extends INestListCfg<ID>>
	extends ICfgWrapper<ID, Cfg> {
	/**
	 * 得到所有的配置
	 * @return 包含所有数据的map
	 */
	Map<ID, List<Cfg>> allCfgs();

	/**
	 * 返回id 对应的list
	 * @param id id
	 * @return 指定id的list数据
	 */
	default List<Cfg> getCfgsById(ID id){
		if (! contains(id)) {
			LoggerType.DUODUO_CFG_READER.info("Cfg [{}] ID [{}] is missing!", getCfgClass().getName(), id);
			return Collections.emptyList();
		}
		return allCfgs().get(id);
	}

	/**
	 * 根据id 和 list的index 取到配置对象
	 * @param id id
	 * @param index 队列的index
	 * @return 对应的cfg
	 */
	default Cfg getCfgsById(ID id, int index){
		if (! contains(id, index)) {
			LoggerType.DUODUO_CFG_READER.info("Cfg [{}] ID [{}] index [{}] is missing!", getCfgClass().getName(), id, index);
			return null;
		}
		return allCfgs().get(id).get(index);
	}

	/**
	 * 是否有该id的配置
	 * @param id 指定id
	 * @return 是否包含list
	 */
	default boolean contains(ID id){
		return allCfgs().containsKey(id);
	}

	/**
	 * 是否有该id的配置
	 * index 是否正确
	 * @param id 指定id
	 * @param index list 中的index
	 * @return true 包含 false 没有
	 */
	default boolean contains(ID id, int index){
		if (! allCfgs().containsKey(id)) {
			return false;
		}
		List<Cfg> cfgsById = getCfgsById(id);
		return index >= 0 && index < cfgsById.size();
	}
}
