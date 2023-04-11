package org.qiunet.cfg.manager.keyval;

import org.qiunet.cfg.annotation.CfgValAutoWired;
import org.qiunet.cfg.base.IKeyValCfg;
import org.qiunet.cfg.event.CfgLoadCompleteEvent;
import org.qiunet.cfg.manager.base.ISimpleMapCfgWrapper;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.convert.ConvertManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/***
 * key val 的管理
 *
 * @author qiunet
 * 2020-09-18 17:18
 */
public enum KeyValManager {
	instance;

	/**
	 * 获得某个key的值
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public long getOrDefault(String key, long defaultVal) {
		KeyValManager0.CfgAutoFieldInfo fieldInfo = KeyValManager0.instance.keyFields.get(key);
		if (fieldInfo == null) {
			return defaultVal;
		}
		return Long.parseLong(fieldInfo.getVal());
	}

	private enum KeyValManager0 implements IApplicationContextAware {
		instance;
		/**
		 * 所有的key -> field 映射
		 */
		final Map<String, CfgAutoFieldInfo> keyFields = new HashMap<>();

		private IApplicationContext context;

		/**
		 * 监听加载完毕事件
		 *
		 * @param eventData
		 */
		@EventListener
		public void cfgManagerLoadEvent(CfgLoadCompleteEvent eventData) {
			if (keyFields.isEmpty()) {
				return;
			}

			eventData.getList().stream().filter(cfg -> IKeyValCfg.class.isAssignableFrom(cfg.getCfgClass()))
					.forEach(cfgManager -> ((ISimpleMapCfgWrapper<String, IKeyValCfg>) cfgManager).allCfgs().forEach((key, data) -> {
				CfgAutoFieldInfo fieldInfo = keyFields.get(key);
				if (fieldInfo == null) {
					return;
				}

				if (fieldInfo.cfgManager != null && fieldInfo.cfgManager != cfgManager) {
					// 已经被别的cfgManager占用了
					throw new CustomException("Key {} is repeated", fieldInfo.getKey());
				}

				if (fieldInfo.cfgManager == null) {
					fieldInfo.cfgManager = (ISimpleMapCfgWrapper<String, IKeyValCfg>) cfgManager;
				}

				fieldInfo.injectVal();
			}));
		}

		@Override
		public int order() {
			return Integer.MAX_VALUE;
		}

		@Override
		public ScannerType scannerType() {
			return ScannerType.KEY_VAL_CFG;
		}

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			this.context = context;

			Set<Field> fieldSet = context.getFieldsAnnotatedWith(CfgValAutoWired.class);
			for (Field field : fieldSet) {
				String keyName = field.getName();
				CfgValAutoWired annotation = field.getAnnotation(CfgValAutoWired.class);
				if (!StringUtil.isEmpty(annotation.key())) {
					keyName = annotation.key();
				}
				keyFields.computeIfAbsent(keyName, CfgAutoFieldInfo::new).addField(field);
			}
		}

		/**
		 * 字段信息值
		 */
		private class CfgAutoFieldInfo {
			/**
			 * 对应的manager
			 */
			ISimpleMapCfgWrapper<String, ? extends IKeyValCfg> cfgManager;
			/**
			 * 需要注入的字段
			 */
			private final List<Field> fields = new LinkedList<>();
			/**
			 * key
			 */
			private final String keyName;


			public CfgAutoFieldInfo(String keyName) {
				this.keyName = keyName;
			}

			/**
			 * 添加field
			 *
			 * @param field
			 */
			void addField(Field field) {
				this.fields.add(field);
			}
			/**
			 * 获得key
			 * @return
			 */
			public String getKey() {
				return keyName;
			}

			/**
			 * 获得值
			 * @return
			 */
			public String getVal() {
				return cfgManager.getCfgById(this.getKey()).val();
			}

			/**
			 * 给字段注入值
			 */
			public void injectVal() {
				fields.forEach(field -> {
					Object realVal = ConvertManager.getInstance().convert(field, this.getVal());
					Object instance = null;
					if (! Modifier.isStatic(field.getModifiers())) {
						instance = context.getInstanceOfClass(field.getDeclaringClass());
					}
					ReflectUtil.setField(instance, field, realVal);
				});
			}
		}
	}
}
