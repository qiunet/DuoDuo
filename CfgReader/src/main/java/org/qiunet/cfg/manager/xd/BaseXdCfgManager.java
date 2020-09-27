package org.qiunet.cfg.manager.xd;

import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.exceptions.CustomException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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
	private ByteArrayInputStream bais;
	private GZIPInputStream gis;
	protected DataInputStream dis;
	protected XdInfoData xdInfoData;

	protected BaseXdCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}
	/**
	 * 获取xd文件
	 * @return 该表的行数量 和 列名称信息
	 * @throws IOException
	 */
	XdInfoData loadXdFileToDataInputStream() throws IOException {
		logger.debug("读取配置文件 [ {}]", fileName);

		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			throw new NullPointerException("fileName "+fileName+" is not exist in classpath");
		}
		if (url.getPath().contains(".jar!")) {
			//jar包里面的文件. 只能用这种加载方式. 缺点是有缓存. 不能热加载设定
			in = getClass().getClassLoader().getResourceAsStream(fileName);
		}else {
			in = new FileInputStream(url.getPath());
		}
		if (in == null) {
			throw new NullPointerException("FileInputStream for "+fileName+" is null!");
		}

		byte [] bytes = new byte[in.available()];
		in.read(bytes);
		CommonUtil.reverse(bytes, 2);
		bais = new ByteArrayInputStream(bytes);
		gis = new GZIPInputStream(bais);
		dis = new DataInputStream(gis);

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
	public void loadCfg() throws Exception{
		this.init();
		this.close();
		this.afterLoad();
	}

	private void close() {
		if (dis != null) {
			boolean readOver = false;
			try {
				dis.readByte();
			}catch (EOFException e) {
				readOver = true;
			} catch (IOException e) {
				throw new CustomException(e, "读取配置文件[{}]数据出现问题", fileName);
			}

			if (! readOver) {
				throw new CustomException("读取配置文件[{}]数据异常 有残留数据", fileName);
			}
		}
		try {
			if(dis != null) {
				dis.close();
			}
			if (gis != null) {
				gis.close();
			}
			if (bais != null) {
				bais.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			throw new CustomException(e, "关闭配置文件[{}]数据出现问题", fileName);
		}
		bais = null;
		gis = null;
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
