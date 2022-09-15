package org.qiunet.cfg.manager.json;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.file.DPath;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author  zhengj
 * Date: 2019/6/6.
 * Time: 10:47.
 * To change this template use File | Settings | File Templates.
 */
abstract class BaseJsonCfgManager<ID, Cfg extends ICfg<ID>> extends BaseCfgManager<ID, Cfg> {
	BaseJsonCfgManager(Class<Cfg> cfgClass) {
		super(cfgClass);
	}

	protected List<Cfg> cfgList;
	@Override
	public void loadCfg() throws Exception {
		this.cfgList = getSimpleListCfg();

		this.init();
		this.afterLoad();
	}

	abstract void init() throws Exception;

	public List<Cfg> list() {
		return cfgList;
	}

	/**
	 * 获取配置文件真实路径
	 *
	 * @param fileName
	 * @return
	 */
	private File [] getFile(String fileName) {
		if(fileName.contains("*")) {
			List<File> files = Lists.newLinkedList();
			String dirName = Objects.requireNonNull(getClass().getClassLoader().getResource(DPath.dirName(fileName))).getFile();
			String finalFileName = DPath.fileName(fileName).replaceAll("\\.", "\\\\.").replaceAll("\\*", "(.*)");
			DPath.listDir(dirName, files::add, file -> file.getName().matches(finalFileName));
			return files.toArray(new File[0]);
		}
		String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile();
		return new File[]{ new File(filePath)};
	}

	/**
	 * json解析成为cfg对象
	 *
	 * @return
	 */
	private List<Cfg> getSimpleListCfg() {
		logger.debug("读取配置文件 [ " + fileName + " ]");
		String json;
		List<JSONObject> jsonObjects = Lists.newLinkedList();
		File [] files = getFile(fileName);
		for (File file : files) {
			this.fileChangeListener(file);
			try {
				json = FileUtil.getFileContent(file);
				if(StringUtil.isEmpty(json)){
					logger.debug("读取配置文件 [{}] content is null!", file.getAbsolutePath());
				}
			} catch (IOException e) {
				throw new CustomException(e, "读取[{}]异常!", file.getAbsolutePath());
			}

			List<JSONObject> generalList = JsonUtil.getGeneralList(json, JSONObject.class);
			if (generalList == null) {
				throw new NullPointerException("FileName ["+file.getAbsolutePath()+"] is not JsonList!");
			}
			jsonObjects.addAll(generalList);
		}

		return jsonObjects.stream().map(this::generalCfg).collect(Collectors.toList());
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
			LoggerType.DUODUO_CFG_READER.error("generalCfg", e);
		}
		final Cfg finalCfg = cfg;
		Preconditions.checkNotNull(cfg);
		jsonObject.forEach((key, val) -> handlerObjConvertAndAssign(finalCfg, key, String.valueOf(val)));
		return cfg;
	}
}
