package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.resource.IResourceCfg0;

import java.util.List;

/***
 * Cfg 加载沙盒 接口.
 *
 * @author qiunet
 * 2023/4/11 16:41
 */
public interface ILoadSandbox {
	/**
	 * 获得资源配置
	 * @param cfgId 资源id
	 * @param <Cfg>
	 * @return
	 */
	<Cfg extends IResourceCfg0> Cfg getResById(int cfgId);
	/**
	 * 从指定的Class里面取到一个
	 * @param clz 类
	 * @param key 对应的Key
	 * @return Cfg or null
	 * @param <ID> ID类型
	 * @param <Cfg> Cfg类型
	 */
	<ID, Cfg extends ISimpleMapCfg<ID>> Cfg getSimpleCfg(Class<Cfg> clz, ID key);

	/**
	 * 从嵌套List的map中获取一个List
	 * @param key 对应key
	 * @return Cfg List or null
	 * @param <ID> ID类型
	 * @param <Cfg> Cfg类型
	 */
	<ID, Cfg extends INestListCfg<ID>> List<Cfg> getNestCfgList(Class<Cfg> clz, ID key);

	/**
	 * 从nest map 取到指定key subKey的cfg
	 *
	 * @param key 对应的key
	 * @param subKey 对应的SubKey
	 * @return Cfg or null
	 * @param <ID> ID类型
	 * @param <SubID> SubID 类型
	 * @param <Cfg> Cfg类型
	 */
	<ID, SubID, Cfg extends INestMapCfg<ID, SubID>> Cfg getNextMapCfg(Class<Cfg> clz, ID key, SubID subKey);
}
