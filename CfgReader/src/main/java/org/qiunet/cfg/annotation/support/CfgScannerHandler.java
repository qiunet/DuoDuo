package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.annotation.CfgWrapperAutoWired;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.wrapper.CfgType;
import org.qiunet.cfg.wrapper.ICfgWrapper;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.classScanner.Singleton;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
@Singleton
public class CfgScannerHandler implements IApplicationContextAware {
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context) throws Exception {
		this.context = context;

		Set<Class<? extends ICfg>> classSet = context.getSubTypesOf(ICfg.class);
		for (Class<?> aClass : classSet) {
			if (aClass.isInterface()
			|| Modifier.isAbstract(aClass.getModifiers())) {
				continue;
			}

			if (! aClass.isAnnotationPresent(Cfg.class)) {
				throw new RuntimeException("Cfg class ["+aClass.getName()+"] must specify Cfg Annotation!");
			}

			CfgType.createCfgWrapper((Class<? extends ICfg>) aClass);
		}

		try {
			CfgManagers.getInstance().initSetting();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}

		Set<Field> fieldSet = context.getFieldsAnnotatedWith(CfgWrapperAutoWired.class);
		for (Field field : fieldSet) {
			if (!ICfgWrapper.class.isAssignableFrom(field.getType())) {
				throw new RuntimeException("CfgWrapperAutoWired must use for Some Type extends ICfgWrapper");
			}

			Type genericType = field.getGenericType();
			if (! ParameterizedType.class.isAssignableFrom(genericType.getClass())){
				throw new RuntimeException("ICfgWrapper must specify GenericType!");
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
}
