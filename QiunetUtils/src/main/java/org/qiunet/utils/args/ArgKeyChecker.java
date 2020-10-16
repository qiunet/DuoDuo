package org.qiunet.utils.args;


import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

			for (Field field : aClass.getFields()) {
				int modifiers = field.getModifiers();

				if (! Modifier.isPublic(modifiers)
				 || ! Modifier.isStatic(modifiers)
				 || ! Modifier.isFinal(modifiers)
				) {
					throw new CustomException("Field[{}#{}] Must be start with public static final!", aClass.getName(), field.getName());
				}
			}
		}
	}
}
