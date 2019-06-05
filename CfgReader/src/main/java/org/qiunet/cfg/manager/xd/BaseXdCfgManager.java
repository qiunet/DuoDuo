package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.ICfgManager;
import org.qiunet.cfg.convert.ICfgTypeConvert;
import org.qiunet.cfg.convert.xd.*;
import org.qiunet.cfg.manager.CfgTypeConvertManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * 大部分数据可以分为:
 * key  -> cfg
 * key -> list<cfg>
 * key -> map<subKey, cfg>
 *
 * Created by qiunet.
 * 17/7/16
 */
abstract class BaseXdCfgManager implements ICfgManager {
	protected final Logger logger = LoggerType.DUODUO.getLogger();
	private InputStream in;
	protected String fileName;
	protected DataInputStream dis;

	protected BaseXdCfgManager(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取xd文件
	 * @return 该表的行数量
	 * @throws IOException
	 */
	int loadXdFileToDataInputStream() throws Exception{
		logger.debug("读取配置文件 [ "+fileName+" ]");

		URL url = getClass().getClassLoader().getResource(fileName);
		if (url.getPath().contains(".jar!")) {
			//jar包里面的文件. 只能用这种加载方式. 缺点是有缓存. 不能热加载设定
			in = getClass().getClassLoader().getResourceAsStream(fileName);
		}else {
			in = new FileInputStream(url.getPath());
		}

		dis = new DataInputStream(in);
		return dis.readInt();
	}
	/**
	 * 初始化设定
	 */
	@Override
	public String loadCfg() {
		String failFileName = "";
		try {
			this.init();
		} catch (Exception e) {
			logger.error("读取配置文件"+fileName+"失败 ERROR:", e);
			failFileName = this.fileName;
		}finally{
			this.close();
			return failFileName;
		}
	}

	private void close() {
		if (dis != null) {
			boolean readOver = false;
			try {
				dis.readByte();
			}catch (EOFException e) {
				readOver = true;
			} catch (IOException e) {
				logger.error("读取配置文件"+fileName+"数据出现问题", e);
			}

			if (! readOver) {
				logger.error("读取配置文件"+fileName+"数据异常 有残留数据", new EOFException());
			}
		}
		try {
			if(dis != null)dis.close();
			if (in != null) in.close();
		} catch (IOException e) {
			logger.error("关闭配置文件"+fileName+"数据出现问题", e);
		}

		fileName = null;
		dis = null;
		in = null;
	}

	abstract void init()throws Exception;
	/***
	 * 通过反射得到一个cfg
	 * @param cfgClass
	 * @param <Cfg>
	 * @return
	 */
	<Cfg> Cfg generalCfg(Class<Cfg> cfgClass) throws Exception {
		Cfg cfg = cfgClass.newInstance();

		Field [] fields = cfgClass.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isPublic(field.getModifiers())
				|| Modifier.isFinal(field.getModifiers())
				|| Modifier.isStatic(field.getModifiers())
				|| Modifier.isTransient(field.getModifiers()))
				continue;

			ICfgTypeConvert convert = returnConert(field.getType());
			Object val = convert.returnObject(field.getName(), dis);
			field.setAccessible(true);
			field.set(cfg, val);
		}
		return cfg;
	}
	private ICfgTypeConvert returnConert(Class type) {
		if (type == Integer.TYPE || type == Integer.class) return intConvert;
		if (type == Boolean.TYPE || type == Boolean.class) return boolConvert;
		if (type == Long.TYPE || type == Long.class) return longConvert;
		if (type == Double.TYPE || type == Double.class) return doubleConvert;
		if (type == String.class) return stringConvert;

		ICfgTypeConvert cfgTypeConvert = CfgTypeConvertManager.getInstance().returnConvert(type);
		if (cfgTypeConvert == null) {
			throw new RuntimeException("not define convert for type ["+type.getName()+"]");
		}
		return cfgTypeConvert;
	}
	private static final ICfgTypeConvert intConvert = new CfgIntegerConvert();
	private static final ICfgTypeConvert boolConvert = new CfgBooleanConvert();
	private static final ICfgTypeConvert longConvert = new CfgLongConvert();
	private static final ICfgTypeConvert doubleConvert = new CfgDoubleConvert();
	private static final ICfgTypeConvert stringConvert = new CfgStringConvert();
	/***
	 * 检查cfg class 不能有set方法
	 * @param cfgClass
	 */
	void checkCfgClass(Class cfgClass) {
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
