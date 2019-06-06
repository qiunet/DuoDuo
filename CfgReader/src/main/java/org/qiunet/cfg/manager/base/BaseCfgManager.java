package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.ICfgManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 15:51.
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseCfgManager implements ICfgManager {

	/***
	 * 检查cfg class 不能有set方法
	 * @param cfgClass
	 */
	public void checkCfgClass(Class cfgClass) {
		for (Field field : cfgClass.getDeclaredFields()) {
			if (Modifier.isPublic(field.getModifiers())
					|| Modifier.isFinal(field.getModifiers())
					|| Modifier.isStatic(field.getModifiers())
					|| Modifier.isTransient(field.getModifiers()))
				continue;

			boolean haveMethod = true;
			try {
				getSetMethod(cfgClass, field);
			} catch (NoSuchMethodException e) {
				haveMethod = false;
			}
			if (haveMethod) {
				throw new RuntimeException("Cfg ["+cfgClass.getName()+"] field ["+field.getName()+"] can not define set method");
			}
		}
	}

	/**
	 * 得到对应的set方法
	 * @param cfgClass
	 * @param field
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Method getSetMethod(Class cfgClass, Field field) throws NoSuchMethodException {
		char [] chars = ("set"+field.getName()).toCharArray();
		chars[3] -= 32;
		String methodName = new String(chars);

		return cfgClass.getMethod(methodName, field.getType());
	}
}
