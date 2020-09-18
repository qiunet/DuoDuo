package org.qiunet.cfg.manager.keyval;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.cfg.annotation.CfgValAutoWired;
import org.qiunet.cfg.base.IKeyValCfg;
import org.qiunet.cfg.convert.CfgFieldObjConvertManager;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.cfg.listener.CfgManagerAddEventData;
import org.qiunet.cfg.manager.base.ICfgManager;
import org.qiunet.cfg.manager.base.ISimpleMapCfgManager;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * key val 的管理
 *
 * @author qiunet
 * 2020-09-18 17:18
 */
class KeyValManager implements IApplicationContextAware {

	private IApplicationContext context;
	/**
	 * key val 的数据
	 */
	private List<ICfgManager> keyValManagers = Lists.newArrayList();
	/**
	 * 监听添加 ICfgManager
	 * @param eventData
	 */
	@EventListener
	public void addCfgManagerEvent(CfgManagerAddEventData eventData) {
		if (! IKeyValCfg.class.isAssignableFrom(eventData.getCfgManager().getCfgClass())) {
			return;
		}

		keyValManagers.add(eventData.getCfgManager());
	}

	/**
	 * 监听加载完毕事件
	 * @param eventData
	 */
	@EventListener
	public void loadCompleteEvent(CfgLoadCompleteEventData eventData) throws IllegalAccessException {
		if (keyValManagers.isEmpty()) {
			return;
		}

		Map<String, String> keyValDatas = Maps.newHashMap();
		for (ICfgManager<IKeyValCfg> keyValManager : keyValManagers) {
			List<IKeyValCfg> list = ((ISimpleMapCfgManager) keyValManager).list();
			for (IKeyValCfg iKeyValCfg : list) {
				Preconditions.checkState(! keyValDatas.containsKey(iKeyValCfg.key()),
					"key [%s] in [%s] cfg is repeated!", iKeyValCfg.key(), keyValManager.getCfgClass().getName());

				keyValDatas.put(iKeyValCfg.key(), iKeyValCfg.val());
			}
		}

		Set<Field> fieldSet = context.getFieldsAnnotatedWith(CfgValAutoWired.class);
		for (Field field : fieldSet) {
			CfgValAutoWired annotation = field.getAnnotation(CfgValAutoWired.class);
			String keyName = annotation.key();
			if (StringUtil.isEmpty(keyName)) {
				keyName = field.getName();
			}

			String val = keyValDatas.get(keyName);
			Preconditions.checkState(! StringUtil.isEmpty(val) , "No cfg value for KeyName [%s]", keyName);
			field.setAccessible(true);
			Object realVal = CfgFieldObjConvertManager.getInstance().covert(field.getType(), val);
			field.set(context.getInstanceOfClass(field.getDeclaringClass()), realVal);
		}
	}

	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;
	}

}
