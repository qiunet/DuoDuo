package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.manager.exception.UnknownFieldException;
import org.qiunet.utils.convert.ConvertManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author  zhengj
 * Date: 2019/6/6.
 * Time: 15:51.
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseCfgManager<Cfg extends ICfg> implements ICfgManager<Cfg> {
	protected Logger logger = LoggerType.DUODUO_CFG_READER.getLogger();

	protected String fileName;

	protected Class<Cfg> cfgClass;
	/**
	 * 加载顺序
	 */
	private final int order;
	/**
	 * 预留一个用户自定义的钩子函数, 可以自己做一些事情
	 * 目前是空的实现,开发者选择是否覆盖函数
	 * 举例: json配置加载完成后,可以进一步对cfg对象做一些处理.初步解析,或者组装数据.方便项目使用配置表.
	 * @throws Exception
	 */
	protected void afterLoad() throws Exception {

	}


	@Override
	public String getLoadFileName() {
		return fileName;
	}

	@Override
	public Class<Cfg> getCfgClass() {
		return cfgClass;
	}


	public BaseCfgManager(Class<Cfg> cfgClass) {
		org.qiunet.cfg.annotation.Cfg cfg = cfgClass.getAnnotation(org.qiunet.cfg.annotation.Cfg.class);
		this.fileName = cfg.value();
		this.cfgClass = cfgClass;
		this.order = cfg.order();

		this.checkCfgClass(cfgClass);
		CfgManagers.getInstance().addCfgManager(this);
	}

	@Override
	public int order() {
		return order;
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
				throw new CustomException("Cfg ["+cfgClass.getName()+"] field ["+field.getName()+"] can not define set method");
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
		Field field = ReflectUtil.findField(cfgClass, name);
		if (field == null) {
			throw new UnknownFieldException(cfgClass.getName(), name);
		}
		try {
			if (isInvalidField(field)) {
				throw new CustomException("Class ["+cfg.getClass().getName()+"] field ["+field.getName()+"] is invalid!");
			}
			Object obj = ConvertManager.getInstance().convert(field, val);
			field.setAccessible(true);
			field.set(cfg, obj);
		} catch (IllegalAccessException e) {
			throw new CustomException(e, "Cfg [{}] name [{}] assign error", cfg.getClass().getName(), name);
		}
	}
}
