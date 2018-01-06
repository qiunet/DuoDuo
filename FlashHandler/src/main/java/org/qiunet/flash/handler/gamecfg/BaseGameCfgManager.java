package org.qiunet.flash.handler.gamecfg;

import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;
import org.qiunet.utils.collection.safe.SafeHashMap;
import org.qiunet.utils.collection.safe.SafeList;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by qiunet.
 * 17/7/16
 */
public abstract class BaseGameCfgManager implements IGameCfgManager {
	protected final QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	private String fileName;
	protected DataInputStream dis;
	private InputStream in;
	private GZIPInputStream gin;
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
		in = getClass().getClassLoader().getResourceAsStream(fileName);
		gin=new GZIPInputStream(in);
		dis = new DataInputStream(gin);
		return dis.readInt();
	}

	/**
	 * 初始化设定
	 */
	@Override
	public boolean loadCfg()throws Exception{
		boolean ret = false;
		try {
			this.init();
			ret = true;
		} catch (Exception e) {
			logger.error("读取配置文件"+fileName+"失败 ERROR:["+e.getMessage()+"]");
		}finally{
			this.close();
		}
		return ret;
	}

	private void close() throws IOException {
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
			if (gin != null) gin.close();
			if (in != null) in.close();
			dis = null;
			gin = null;
			in = null;
		} catch (IOException e) {
			logger.error("关闭配置文件"+fileName+"数据出现问题", e);
		}
	}

	protected abstract void init()throws Exception;

	/***
	 * 得到有序的map
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
			cfgMap.put(cfg.getKey(), cfg);
		}
		cfgMap.safeLock();
		return cfgMap;
	}

	/***
	 * 得到一个一定格式的嵌套map
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
			subMap.put(cfg.getSubKey(), cfg);
		}
		for (Map<SubKey, Cfg> subKeyCfgMap : cfgMap.values()) {
			((SafeHashMap) subKeyCfgMap).safeLock();
		}
		cfgMap.safeLock();
		return cfgMap;
	}

	/***
	 * 得到嵌套list的map数据
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
		cfgMap.safeLock();
		return cfgMap;
	}
}
