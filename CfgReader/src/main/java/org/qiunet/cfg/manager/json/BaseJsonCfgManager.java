package org.qiunet.cfg.manager.json;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.exceptions.CustomException;
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
abstract class BaseJsonCfgManager<Cfg extends ICfg> extends BaseCfgManager<Cfg> {
	BaseJsonCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	@Override
	public void loadCfg() throws Exception {
		this.init();
		this.afterLoad();
	}

	abstract void init() throws Exception;

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
		String json;
		File file = getFile(fileName);
		try {
			json = FileUtil.getFileContent(getFile(fileName));
		} catch (IOException e) {
			throw new CustomException(e, "读取[{}]异常!", file.getAbsolutePath());
		}

		List<JSONObject> generalList = JsonUtil.getGeneralList(json, JSONObject.class);
		if (generalList == null) {
			throw new NullPointerException("FileName ["+fileName+"] is not JsonList!");
		}
		return generalList.stream().map(this::generalCfg).collect(Collectors.toList());
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
		Preconditions.checkNotNull(cfg);
		jsonObject.forEach((key, val) -> handlerObjConvertAndAssign(finalCfg, key, String.valueOf(val)));
		return cfg;
	}
}
