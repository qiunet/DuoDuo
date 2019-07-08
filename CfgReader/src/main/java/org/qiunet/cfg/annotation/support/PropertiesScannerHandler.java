package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.annotation.Properties;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.properties.LoaderProperties;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @author qiunet
 *         Created on 17/2/9 15:43.
 */
public class PropertiesScannerHandler implements IApplicationContextAware {
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context) {
		this.context = context;

		Set<Class<? extends LoaderProperties>> set = context.getSubTypesOf(LoaderProperties.class);
		set.stream().filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
			.forEach(this::handler);
	}

	private void handler(Class<?> clazz) {
		Properties setting = clazz.getAnnotation(Properties.class);
		try {
			LoaderProperties properties = (LoaderProperties) context.getInstanceOfClass(clazz);
			CfgManagers.getInstance().addPropertySetting(properties , setting == null ? 0 : setting.order());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
