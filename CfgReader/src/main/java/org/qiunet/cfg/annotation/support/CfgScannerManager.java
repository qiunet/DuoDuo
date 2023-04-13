package org.qiunet.cfg.annotation.support;

import com.google.common.collect.Maps;
import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.event.CfgPrepareOverEvent;
import org.qiunet.cfg.manager.base.ICfgWrapper;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.scanner.anno.AutoWired;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
enum CfgScannerManager implements IApplicationContextAware {
	instance;
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;

		this.createCfgWrapper();
		this.cfgAutoWired();
		this.initCfg();
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.CFG_SCANNER;
	}

	@Override
	public int order() {
		return 10;
	}

	private void initCfg() {
		CfgPrepareOverEvent.instance.fireEventHandler();
	}
	/**
	 * 所有的 manager
	 */
	private final Map<Class, ICfgWrapper> map = Maps.newHashMap();

	/**
	 * 创建cfg 读取 manager.
	 */
	private void createCfgWrapper(){
		Set<Class<? extends ICfg>> classSet = context.getSubTypesOf(ICfg.class);
		for (Class<? extends ICfg> aClass : classSet) {
			if (aClass.isInterface()
					|| Modifier.isAbstract(aClass.getModifiers())) {
				continue;
			}

			if (! aClass.isAnnotationPresent(Cfg.class)) {
				throw new CustomException("Cfg class [{}] must specify Cfg Annotation!", aClass.getName());
			}

			map.put(aClass, CfgType.createCfgWrapper(aClass));
		}
	}

	/**
	 * 自动注入
	 */
	private void cfgAutoWired() throws Exception {
		Set<Field> fieldSet = context.getFieldsAnnotatedWith(AutoWired.class);
		for (Field field : fieldSet) {
			if (! ICfgWrapper.class.isAssignableFrom(field.getType())) {
				continue;
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

			ReflectUtil.makeAccessible(field).set(obj, map.get(cfgClass));
		}
	}
}
