package org.qiunet.cfg.manager.base;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.cfg.base.*;
import org.qiunet.cfg.event.CfgLoadCompleteEvent;
import org.qiunet.cfg.event.CfgManagerAddEvent;
import org.qiunet.cfg.resource.IResourceCfg0;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * 加载沙盒
 *
 * @author qiunet
 * 2023/4/11 17:10
 */
public enum LoadSandbox implements ILoadSandbox {
	instance;
	/**
	 * 数据的wrapper
	 */
	private final Map<Class<? extends ICfg<?>>, ICfgWrapper<?, ?>> dataWrappers = Maps.newConcurrentMap();
	/**
	 * 保存所有cfg manager
	 */
	private final Map<Class<? extends ICfg<?>>, ICfgManager<?, ?>> managers = Maps.newConcurrentMap();
	/**
	 * 包含的资源的class
	 */
	private final Set<Class<? extends IResourceCfg0>> resourceCfgClasses = Sets.newConcurrentHashSet();
	/**
	 * 添加数据到沙盒
	 * @param wrapper 沙盒wrapper
	 */
	void addWrapper(ICfgWrapper<?, ?> wrapper) {
		dataWrappers.put(wrapper.getCfgClass(), wrapper);
	}

	@Override
	public <Cfg extends IResourceCfg0> Cfg getResById(int cfgId) {
		for (Class<? extends IResourceCfg0> cfgClass : resourceCfgClasses) {
			ISimpleMapCfgWrapper<Integer, Cfg> wrapper = (ISimpleMapCfgWrapper<Integer, Cfg>) dataWrappers.get(cfgClass);
			if (wrapper != null) {
				if (wrapper.contains(cfgId)) {
					return wrapper.getCfgById(cfgId);
				}
				continue;
			}
			ISimpleMapCfgWrapper<Integer, Cfg> manager = (ISimpleMapCfgWrapper<Integer, Cfg>) managers.get(cfgClass);
			if (manager.contains(cfgId)) {
				return manager.getCfgById(cfgId);
			}
		}
		return null;
	}

	@EventListener
	private void addManager(CfgManagerAddEvent event) {
		Class<? extends ICfg<?>> cfgClass = event.getCfgManager().getCfgClass();
		if (IResourceCfg0.class.isAssignableFrom(cfgClass)) {
			resourceCfgClasses.add((Class<? extends IResourceCfg0>) cfgClass);
		}
		managers.put(cfgClass, event.getCfgManager());
	}

	@Override
	public <ID, Cfg extends ISimpleMapCfg<ID>> Cfg getSimpleCfg(Class<Cfg> clz, ID key) {
		ICfgWrapper<?, ?> iCfgWrapper = dataWrappers.get(clz);
		if (iCfgWrapper != null) {
			return ((ISimpleMapCfgWrapper<ID, Cfg>) iCfgWrapper).getCfgById(key);
		}
		return ((ISimpleMapCfgWrapper<ID, Cfg>) managers.get(clz)).getCfgById(key);
	}

	@Override
	public <ID, Cfg extends INestListCfg<ID>> List<Cfg> getNestCfgList(Class<Cfg> clz, ID key) {
		ICfgWrapper<?, ?> iCfgWrapper = dataWrappers.get(clz);
		if (iCfgWrapper != null) {
			return ((INestListCfgWrapper<ID, Cfg>) iCfgWrapper).getCfgsById(key);
		}
		return ((INestListCfgWrapper<ID, Cfg>) managers.get(clz)).getCfgsById(key);
	}

	@Override
	public <ID, SubID, Cfg extends INestMapCfg<ID, SubID>> Cfg getNextMapCfg(Class<Cfg> clz, ID key, SubID subKey) {
		ICfgWrapper<?, ?> iCfgWrapper = dataWrappers.get(clz);
		if (iCfgWrapper != null) {
			return ((INestMapCfgWrapper<ID, SubID, Cfg>) iCfgWrapper).getCfgById(key, subKey);
		}
		return ((INestMapCfgWrapper<ID, SubID, Cfg>) managers.get(clz)).getCfgById(key, subKey);
	}

	/**
	 * 尝试从 wrapper 里面获取数据
	 * @param cfgClass clz
	 * @return cfg list
	 * @throws Exception 字段什么的异常
	 */
	private List<ICfg<?>> getDataFromWrapper(Class cfgClass) throws Exception{
		// 因为外面做校验了, 所以肯定不会为null!
		ICfgWrapper<?, ?> iCfgWrapper = dataWrappers.get(cfgClass);
		assert iCfgWrapper != null;

		List<ICfg<?>> cfgList = (List<ICfg<?>>) iCfgWrapper.list();
		if (cfgList.isEmpty()) {
			return cfgList;
		}

		// 处理所有数据
		List<Field> delayLoadDataFieldList = ((BaseCfgManager<?, ?>) managers.get(cfgClass)).delayLoadFields;
		for (Field field : delayLoadDataFieldList) {
			for (ICfg<?> cfg : cfgList) {
				((ICfgDelayLoadData) ReflectUtil.makeAccessible(field).get(cfg)).loadData();
			}
		}

		return cfgList;
	}

	@EventListener(EventHandlerWeightType.HIGHEST)
	private void loadComplete(CfgLoadCompleteEvent data) {
		// 需要全部check. 可能删除了已有关联性.
		// 先init完毕. 再check .避免check调用其它cfg内容. 但是load里面没有赋值.
		Set<? extends Class> classSet = data.getList().stream().map(ICfgWrapper::getCfgClass).collect(Collectors.toSet());
		try {
			for (Class<? extends ICfg<?>> cfgClass : managers.keySet()) {
				List<ICfg<?>> cfgList = null;
				// 只有本次加载的配置, 才会去读取wrapper
				if (classSet.contains(cfgClass)) {
					cfgList = getDataFromWrapper(cfgClass);
				}

				if (cfgList == null) {
					cfgList = (List<ICfg<?>>) managers.get(cfgClass).list();
				}

				if (cfgList == null) {
					throw new CustomException("Cfg class {} not container data list!", cfgClass.getSimpleName());
				}

				if (ICfgCustomInit.class.isAssignableFrom(cfgClass)) {
					cfgList.forEach(ele -> ((ICfgCustomInit) ele).init(this));
				}

				if (ICfgCheck.class.isAssignableFrom(cfgClass)) {
					cfgList.forEach(ele -> ((ICfgCheck) ele).check(this));
				}
			}
		}catch (Exception e){
			// 清理掉当前的wrapper.
			data.getList().forEach(m -> this.dataWrappers.remove(m.getCfgClass()));
			throw new CustomException(e, "Sandbox exception:");
		}

		this.clearAndReplaceData(data.getList());
	}

	/**
	 * 清理然后替换指定的 data
	 * @param list 指定的list
	 */
	private void clearAndReplaceData(List<ICfgWrapper> list) {
		for (ICfgWrapper manager : list) {
			ICfgWrapper wrapper = dataWrappers.remove(manager.getCfgClass());
			if (wrapper == null) {
				throw new CustomException("Null point exception!");
			}
			((BaseCfgManager) manager).switchCfgToRuntime(wrapper);
		}
	}
}
