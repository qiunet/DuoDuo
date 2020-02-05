package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.base.ICfgManager;
import org.qiunet.cfg.convert.BaseObjConvert;
import org.qiunet.cfg.convert.CfgFieldObjConvertManager;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;

/**
 * @author  zhengj
 * Date: 2019/6/6.
 * Time: 15:51.
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseCfgManager<Cfg extends ICfg> implements ICfgManager {
	protected Logger logger = LoggerType.DUODUO.getLogger();

	protected String fileName;

	protected Class<Cfg> cfgClass;

	/**
	 * 预留一个用户自定义的钩子函数, 可以自己做一些事情
	 * 目前是空的实现,开发者选择是否覆盖函数
	 * 举例: json配置加载完成后,可以进一步对cfg对象做一些处理.初步解析,或者组装数据.方便项目使用配置表.
	 * @throws Exception
	 */
	protected void initBySelf() throws Exception {

	}

	public BaseCfgManager(String fileName) {
		Type type = getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
			throw new RuntimeException("Class ["+getClass().getName()+"] 必须给定泛型!");
		}

		Type[] actualTypeArguments = ((ParameterizedTypeImpl) type).getActualTypeArguments();
		this.cfgClass = (Class<Cfg>) actualTypeArguments[actualTypeArguments.length - 1];
		this.checkCfgClass(cfgClass);

		org.qiunet.cfg.annotation.Cfg annotation = getClass().getAnnotation(org.qiunet.cfg.annotation.Cfg.class);
		CfgManagers.getInstance().addDataSettingManager(this, annotation == null ? 0 : annotation.order());

		this.fileName = fileName;
	}

	/***
	 * 检查cfg class 不能有set方法
	 * @param cfgClass
	 */
	private void checkCfgClass(Class cfgClass) {
		for (Field field : cfgClass.getDeclaredFields()) {
			if (isInvalidField(field)) {
				continue;
			}
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
	 * 判断field的有效性.
	 * @param field
	 * @return
	 */
	private boolean isInvalidField(Field field) {
		return Modifier.isPublic(field.getModifiers())
			|| Modifier.isFinal(field.getModifiers())
			|| Modifier.isStatic(field.getModifiers())
			|| Modifier.isTransient(field.getModifiers());
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

	/***\
	 * 转换字符串为对象. 并且赋值给字段
	 * @param cfg 配置文件对象
	 * @param name 字段名称
	 * @param val 字符串值
	 * @param <Cfg> 配置文件类
	 */
	protected <Cfg extends ICfg> void handlerObjConvertAndAssign(Cfg cfg, String name, String val) {
		try {
			Field field = cfg.getClass().getDeclaredField(name);
			if (isInvalidField(field)) {
				throw new RuntimeException("Class ["+cfg.getClass().getName()+"] field ["+field.getName()+"] is invalid!");
			}
			Class<?> aClass = field.getType();
			Object obj = this.covert(cfg.getClass(), aClass, val);
			field.setAccessible(true);
			field.set(cfg, obj);
		} catch (NoSuchFieldException e) {
			logger.error("Cfg ["+cfg.getClass().getName()+"] name ["+name+"] not define", e);
		} catch (IllegalAccessException e) {
			logger.error("Cfg ["+cfg.getClass().getName()+"] name ["+name+"] assign error", e);
		}
	}

	/***
	 * 按照指定的class 类型转换str
	 * @param clazz
	 * @param val
	 * @return 没有转换器将抛出异常
	 */
	public Object covert(Class cfg, Class clazz, String val) {
		for (BaseObjConvert convert : CfgFieldObjConvertManager.getInstance().getConverts()) {
			if (convert.canConvert(clazz)) {
				return convert.fromString(val);
			}
		}
		throw new RuntimeException("Can not convert class type for ["+clazz.getName()+"] in class ["+cfg.getName()+"]");
	}
}
