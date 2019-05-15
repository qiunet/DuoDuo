package org.qiunet.flash.handler.gamecfg;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.collection.safe.SafeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

/**
 * 大部分数据可以分为:
 * key  -> cfg
 * key -> list<cfg>
 * key -> map<subKey, cfg>
 *
 * Created by qiunet.
 * 17/7/16
 */
public abstract class BaseGameCfgManager implements IGameCfgManager {
	protected final Logger logger = LoggerType.DUODUO.getLogger();
	private String fileName;
	protected DataInputStream dis;
	private InputStream in;
	/**
	 * 获取xd文件
	 * @param fileName
	 * @return 该表的行数量
	 * @throws IOException
	 */
	protected int loadXdFileToDataInputStream(String fileName) throws Exception{
		this.close();//  如果读取的多文件.

		this.fileName = fileName;
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

	protected abstract void init()throws Exception;

	/***
	 * 得到的map
	 * Map<Key, Cfg>
	 * @param filePath
	 * @param cfgClass
	 * @param <Key>
	 * @param <Cfg>
	 * @return
	 * @throws Exception
	 */
	protected  <Key, Cfg extends ISimpleMapConfig<Key>> Map<Key, Cfg> getSimpleMapCfg(String filePath, Class<Cfg> cfgClass) throws Exception{
		int num = loadXdFileToDataInputStream(filePath);
		SafeHashMap<Key, Cfg> cfgMap = new SafeHashMap<>();
		for (int i = 0 ; i < num; i++ ) {
			Constructor<Cfg> cfgConstructor = cfgClass.getDeclaredConstructor(DataInputStream.class);
			cfgConstructor.setAccessible(true);
			Cfg cfg = cfgConstructor.newInstance(dis);
			if (cfgMap.containsKey(cfg.getKey())) {
				throw new RuntimeException("Key ["+cfg.getKey()+"] is duplicate!");
			}
			cfgMap.put(cfg.getKey(), cfg);
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}

	/***
	 * 得到一个简单的list<cfg>
	 * @param filePath
	 * @param cfgClass
	 * @param <Cfg>
	 * @throws Exception
	 */
	protected <Cfg> List<Cfg> getSimpleListCfg(String filePath, Class<Cfg> cfgClass) throws Exception {
		int num = loadXdFileToDataInputStream(filePath);
		List<Cfg> list = new SafeList<>();
		for (int i = 0; i < num; i++) {
			Constructor<Cfg> cfgConstructor = cfgClass.getDeclaredConstructor(DataInputStream.class);
			cfgConstructor.setAccessible(true);
			Cfg cfg = cfgConstructor.newInstance(dis);
			list.add(cfg);
		}
		((SafeList<Cfg>) list).safeLock();
		return list;
	}
	/***
	 * 得到一个一定格式的嵌套map
	 * 格式: key 对应 Map<subKey, cfg>
	 * @param fileName
	 * @param cfgClass
	 * @param <Key>
	 * @param <SubKey>
	 * @param <Cfg>
	 * @return
	 * @throws Exception
	 */
	protected <Key, SubKey, Cfg extends INestMapConfig<Key,SubKey>> Map<Key, Map<SubKey, Cfg>> getNestMapCfg(String fileName , Class<Cfg> cfgClass) throws Exception {
		SafeHashMap<Key, Map<SubKey, Cfg>> cfgMap = new SafeHashMap<>();
		int num = loadXdFileToDataInputStream(fileName);
		for (int i = 0; i < num; i++) {
			Constructor<Cfg> cfgConstructor = cfgClass.getDeclaredConstructor(DataInputStream.class);
			cfgConstructor.setAccessible(true);
			Cfg cfg = cfgConstructor.newInstance(dis);

			Map<SubKey, Cfg> subMap = cfgMap.get(cfg.getKey());
			if (subMap == null) {
				subMap = new SafeHashMap<>();
				cfgMap.put(cfg.getKey(), subMap);
			}

			if (subMap.containsKey(cfg.getSubKey())) {
				throw new RuntimeException("SubKey ["+cfg.getSubKey()+"] is duplicate!");
			}

			subMap.put(cfg.getSubKey(), cfg);
		}
		for (Map<SubKey, Cfg> subKeyCfgMap : cfgMap.values()) {
			((SafeHashMap) subKeyCfgMap).loggerIfAbsent();
			((SafeHashMap) subKeyCfgMap).safeLock();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}

	/***
	 * 得到嵌套list的map数据
	 * 一个key  对应一个 cfg list的结构
	 * @param fileName
	 * @param cfgClass
	 * @param <Key>
	 * @param <Cfg>
	 * @return
	 * @throws Exception
	 */
	protected <Key, Cfg extends INestListConfig<Key>> Map<Key, List<Cfg>> getNestListCfg(String fileName , Class<Cfg> cfgClass) throws Exception {
		SafeHashMap<Key, List<Cfg>> cfgMap = new SafeHashMap<>();
		int num = loadXdFileToDataInputStream(fileName);
		for (int i = 0; i < num; i++) {
			Constructor<Cfg> cfgConstructor = cfgClass.getDeclaredConstructor(DataInputStream.class);
			cfgConstructor.setAccessible(true);
			Cfg cfg = cfgConstructor.newInstance(dis);

			List<Cfg> subList = cfgMap.get(cfg.getKey());
			if (subList == null) {
				subList = new SafeList<>();
				cfgMap.put(cfg.getKey(), subList);
			}
			subList.add(cfg);
		}
		for (List<Cfg> cfgList : cfgMap.values()) {
			((SafeList) cfgList).safeLock();
		}
		cfgMap.loggerIfAbsent();
		cfgMap.safeLock();
		return cfgMap;
	}
}
