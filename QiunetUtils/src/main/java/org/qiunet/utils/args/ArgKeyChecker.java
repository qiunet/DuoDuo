package org.qiunet.utils.args;


import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/***
 *
 *  对所有argKey 做检查.
 *
 * @author qiunet
 * 2020-08-26 08:24
 **/
class ArgKeyChecker implements IApplicationContextAware {
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<? extends IArgKey>> subTypesOf = context.getSubTypesOf(IArgKey.class);
		for (Class<? extends IArgKey> aClass : subTypesOf) {
			if (Modifier.isAbstract(aClass.getModifiers())) {
				continue;
			}

			Set<String> names = new HashSet<>();
			for (Field field : aClass.getFields()) {
				int modifiers = field.getModifiers();
				if (! Modifier.isPublic(modifiers)
				 || ! Modifier.isStatic(modifiers)
				) {
					continue;
				}

				IArgKey o = (IArgKey) field.get(null);
				if (names.contains(o.name())) {
					throw new IllegalStateException("IArgKey name ["+o.name()+"] in class ["+getClass().getName()+"] is duplicate");
				}
				names.add(o.name());
			}
		}
	}
}
