package org.qiunet.cfg.manager.json;

import com.alibaba.fastjson.JSONObject;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.json.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  zhengj
 * Date: 2019/6/6.
 * Time: 10:47.
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseJsonCfgManager<Cfg extends ICfg> extends BaseCfgManager<Cfg> {
	BaseJsonCfgManager(String fileName) {
		super(fileName);
	}

	@Override
	public String loadCfg() {
		String failFileName = "";
		try {
			this.init();
		} catch (Exception e) {
			logger.error("读取配置文件" + fileName + "失败 ERROR:", e);
			failFileName = this.fileName;
		} finally {
			return failFileName;
		}
	}

	abstract void init() throws Exception;

	/**
	 * 预留一个用户自定义的钩子函数, 可以自己做一些事情
	 * 目前是空的实现,开发者选择是否覆盖函数
	 * 举例: json配置加载完成后,可以进一步对cfg对象做一些处理.初步解析,或者组装数据.方便项目使用配置表.
	 * @throws Exception
	 */
	public void initBySelf() throws Exception {

	}

	/**
	 * 获取配置文件真实路径
	 *
	 * @param fileName
	 * @return
	 */
	protected File getFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}

	/**
	 * json解析成为cfg对象
	 *
	 * @return
	 */
	protected List<Cfg> getSimpleListCfg() {
		logger.debug("读取配置文件 [ " + fileName + " ]");
		String json = null;
		try {
			json = FileUtil.getFileContent(getFile(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<JSONObject> generalList = JsonUtil.getGeneralList(json, JSONObject.class);
		if (generalList == null) {
			throw new NullPointerException("FileName ["+fileName+"] is not JsonList!");
		}
		return generalList.stream().map(jsonObject -> generalCfg(jsonObject)).collect(Collectors.toList());
	}


	/***
	 * 通过反射得到一个cfg
	 * @return
	 */
	protected Cfg generalCfg(JSONObject jsonObject) {
		Cfg cfg = null;
		try {
			cfg = cfgClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		final Cfg finalCfg = cfg;
		jsonObject.forEach((key, val) -> handlerObjConvertAndAssign(finalCfg, key, String.valueOf(val)));
		return cfg;
	}
}
