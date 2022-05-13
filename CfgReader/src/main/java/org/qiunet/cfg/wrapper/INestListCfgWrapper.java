package org.qiunet.cfg.wrapper;

import com.google.common.collect.Lists;
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
	 * @return
	 */
	Map<ID, List<Cfg>> allCfgs();

	/**
	 * 返回id 对应的list
	 * @param id
	 * @return
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
	 * @param id
	 * @param index 队列的index
	 * @return
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
	 * @param id
	 * @return
	 */
	default boolean contains(ID id){
		return allCfgs().containsKey(id);
	}

	/**
	 * 是否有该id的配置
	 * index 是否正确
	 * @param id
	 * @return
	 */
	default boolean contains(ID id, int index){
		if (! allCfgs().containsKey(id)) {
			return false;
		}
		List<Cfg> cfgsById = getCfgsById(id);
		return index >= 0 && index < cfgsById.size();
	}

	@Override
	default List<Cfg> list(){
		Map<ID, List<Cfg>> allCfgs = allCfgs();
		List<Cfg> cfgList = Lists.newArrayList();

		for (List<Cfg> list : allCfgs.values()) {
			cfgList.addAll(list);
		}
		return cfgList;
	}

	@Override
	default int size(){
		return list().size();
	}
}
