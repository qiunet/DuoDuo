package org.qiunet.cfg.annotation.support;

import com.google.common.collect.Maps;
import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.annotation.CfgLoadOver;
import org.qiunet.cfg.annotation.CfgWrapperAutoWired;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.wrapper.CfgType;
import org.qiunet.cfg.wrapper.ICfgWrapper;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Set;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
enum CfgScannerManager implements IApplicationContextAware {
	instance;
	private Map<Class<? extends ICfg>, LoadOverMethod> loadOverMap = Maps.newHashMap();
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;
		this.scannerLoadOverMethod();
		this.createCfgWrapper();
		this.cfgAutoWired();
		this.initCfg();

	}
	private void initCfg() {
		CfgManagers.getInstance().initSetting();
	}
	/**
	 * 扫描所有cfgLoadOver method
	 */
	private void scannerLoadOverMethod() {
		Set<Method> methods = context.getMethodsAnnotatedWith(CfgLoadOver.class);
		for (Method method : methods) {
			CfgLoadOver annotation = method.getAnnotation(CfgLoadOver.class);
			loadOverMap.put(annotation.value(), new LoadOverMethod(context.getInstanceOfClass(method.getDeclaringClass()), method));
		}
	}

	/**
	 * 创建cfg 读取 manager.
	 */
	private void createCfgWrapper(){
		Set<Class<? extends ICfg>> classSet = context.getSubTypesOf(ICfg.class);
		for (Class<?> aClass : classSet) {
			if (aClass.isInterface()
					|| Modifier.isAbstract(aClass.getModifiers())) {
				continue;
			}

			if (! aClass.isAnnotationPresent(Cfg.class)) {
				throw new CustomException("Cfg class [{}] must specify Cfg Annotation!", aClass.getName());
			}

			CfgType.createCfgWrapper((Class<? extends ICfg>) aClass);
		}
	}

	/**
	 * 自动注入
	 */
	private void cfgAutoWired() throws Exception {
		Set<Field> fieldSet = context.getFieldsAnnotatedWith(CfgWrapperAutoWired.class);
		for (Field field : fieldSet) {
			if (!ICfgWrapper.class.isAssignableFrom(field.getType())) {
				throw new CustomException("CfgWrapperAutoWired must use for Some Type extends ICfgWrapper");
			}

			Type genericType = field.getGenericType();
			if (! ParameterizedType.class.isAssignableFrom(genericType.getClass())){
				throw new CustomException("ICfgWrapper must specify GenericType!");
			}

			Object obj = null;
			if (! Modifier.isStatic(field.getModifiers())) {
				obj = context.getInstanceOfClass(field.getDeclaringClass());
			}
			Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
			Class<? extends ICfg> cfgClass = (Class<? extends ICfg>) types[types.length - 1];

			field.setAccessible(true);
			field.set(obj, CfgType.getCfgWrapper(cfgClass));
		}
	}

	@EventListener(EventHandlerWeightType.LESS)
	private void completeLoader(CfgLoadCompleteEventData data) {
		loadOverMap.values().forEach(LoadOverMethod::call);
	}

	private static class LoadOverMethod {
		private Object object;
		private Method method;

		public LoadOverMethod(Object object, Method method) {
			this.object = object;
			this.method = method;
			if (method.getParameterCount() > 0) {
				throw new CustomException("LoadOver method not need parameter!");
			}
			ReflectUtil.makeAccessible(method);
		}

		public void call() {
			try {
				method.invoke(object);
			} catch (Exception e) {
				throw new CustomException(e, "load over method call error!");
			}
		}
	}
}