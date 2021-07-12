package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.annotation.CfgIgnore;
import org.qiunet.cfg.convert.ICfgTypeConvert;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.manager.CfgTypeConvertManager;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
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
abstract class BaseXdCfgManager extends BaseCfgManager {
	protected final Logger logger = LoggerType.DUODUO.getLogger();
	private InputStream in;
	protected String fileName;
	protected DataInputStream dis;

	protected BaseXdCfgManager(String fileName) {
		Cfg annotation = getClass().getAnnotation(Cfg.class);
		CfgManagers.getInstance().addDataSettingManager(this, annotation == null? 0: annotation.order());
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

//		fileName = null;
		dis = null;
		in = null;
	}

	abstract void init()throws Exception;

	/**
	 * 预留一个用户自定义的钩子函数, 可以自己做一些事情
	 * 目前是空的实现,开发者选择是否覆盖函数
	 * 举例: json配置加载完成后,可以进一步对cfg对象做一些处理.初步解析,或者组装数据.方便项目使用配置表.
	 * @throws Exception
	 */
	public void initBySelf() throws Exception {

	}

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
				|| Modifier.isTransient(field.getModifiers())
				|| field.isAnnotationPresent(CfgIgnore.class))
				continue;
			Object val;
			Class<?> type = field.getType();
			if (type == Integer.TYPE || type == Integer.class) val = dis.readInt();
			else if (type == Boolean.TYPE || type == Boolean.class) val = dis.readInt() == 1;
			else if (type == Long.TYPE || type == Long.class) val = dis.readLong();
			else if (type == Double.TYPE || type == Double.class) val = dis.readDouble();
			else if (type == String.class) val = dis.readUTF();
			else {
				ICfgTypeConvert convert = returnConvert(type);
				val = convert.returnObject(field.getName(), dis);
			}
			field.setAccessible(true);
			field.set(cfg, val);
		}
		return cfg;
	}
	private ICfgTypeConvert returnConvert(Class type) {
		ICfgTypeConvert cfgTypeConvert = CfgTypeConvertManager.getInstance().returnConvert(type);
		if (cfgTypeConvert == null) {
			throw new RuntimeException("not define convert for type ["+type.getName()+"]");
		}
		return cfgTypeConvert;
	}
}
