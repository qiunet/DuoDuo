package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 大部分数据可以分为:
 * key  -> cfg
 * key -> list<cfg>
 * key -> map<subKey, cfg>
 *
 * Created by qiunet.
 * 17/7/16
 */
abstract class BaseXdCfgManager<Cfg extends ICfg> extends BaseCfgManager<Cfg> {
	private InputStream in;
	protected DataInputStream dis;
	protected XdInfoData xdInfoData;

	protected BaseXdCfgManager(String fileName) {
		super(fileName);
	}
	/**
	 * 获取xd文件
	 * @return 该表的行数量 和 列名称信息
	 * @throws IOException
	 */
	XdInfoData loadXdFileToDataInputStream() throws Exception{
		logger.debug("读取配置文件 [ "+fileName+" ]");

		URL url = getClass().getClassLoader().getResource(fileName);
		if (url.getPath().contains(".jar!")) {
			//jar包里面的文件. 只能用这种加载方式. 缺点是有缓存. 不能热加载设定
			in = getClass().getClassLoader().getResourceAsStream(fileName);
		}else {
			in = new FileInputStream(url.getPath());
		}

		dis = new DataInputStream(in);
		int rowNum = dis.readInt();
		List<String> names = new ArrayList<>();
		int nameLength = dis.readShort();
		for (int i = 0; i < nameLength; i++) {
			names.add(dis.readUTF());
		}

		this.xdInfoData = new XdInfoData(rowNum, names);
		return this.xdInfoData;
	}
	/**
	 * 初始化设定
	 */
	@Override
	public String loadCfg() {
		String failFileName = "";
		try {
			this.init();
			this.initBySelf();
		} catch (Exception e) {
			logger.error("读取配置文件"+fileName+"失败 ERROR:", e);
			failFileName = this.fileName;
		}finally{
			this.close();
		}
		return failFileName;
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
			if(dis != null) {
				dis.close();
			}
			if (in != null) {
				in.close();
			}
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
	 * @return
	 */
	Cfg generalCfg() throws Exception {
		Cfg cfg = cfgClass.newInstance();

		for (String name: xdInfoData.getNames()) {
			this.handlerObjConvertAndAssign(cfg, name, dis.readUTF());
		}
		return cfg;
	}
}
