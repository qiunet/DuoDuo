package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.annotation.CfgWrapperAutoWired;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.wrapper.CfgType;
import org.qiunet.cfg.wrapper.ICfgWrapper;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

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
public class CfgScannerHandler implements IApplicationContextAware {
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;

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

		CfgManagers.getInstance().initSetting();
	}
}
