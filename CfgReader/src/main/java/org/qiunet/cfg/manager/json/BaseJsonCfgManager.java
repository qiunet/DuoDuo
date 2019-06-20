package org.qiunet.cfg.manager.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.cfg.manager.base.BaseCfgManager;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengj
 * Date: 2019/6/6.
 * Time: 10:47.
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseJsonCfgManager extends BaseCfgManager {
	protected final Logger logger = LoggerType.DUODUO.getLogger();
	private String fileName;

	protected BaseJsonCfgManager(String fileName) {
		Cfg annotation = getClass().getAnnotation(Cfg.class);
		CfgManagers.getInstance().addDataSettingManager(this, annotation == null? 0: annotation.order());
		this.fileName = fileName;
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
	 * 获取配置文件真实路径
	 * @param fileName
	 * @return
	 */
	protected File getFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}

	/**
	 * json解析成为cfg对象
	 * @param sheetName
	 * @param cfgClass
	 * @param <Cfg>
	 * @return
	 */
	protected <Cfg> List<Cfg> getSimpleListCfg(String sheetName, Class<Cfg> cfgClass) throws Exception{
		logger.debug("读取配置文件 [ " + fileName + " ]");
		String json = null;
		try {
			json = FileUtil.getFileContent(getFile(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (StringUtil.isEmpty(sheetName)) {
			List<JSONObject> generalList = JsonUtil.getGeneralList(json, JSONObject.class);

			List<Cfg> reList = new ArrayList<>();

			for (JSONObject jsonObject : generalList) {
				reList.add(generalCfg(jsonObject,cfgClass));
			}
			return reList;
//			return JsonUtil.getGeneralList(json, cfgClass);
		}

		JSONObject jsonObject = JsonUtil.getGeneralObject(json, JSONObject.class);
		return ((JSONArray) (jsonObject.get(sheetName))).toJavaList(cfgClass);
	}


	/***
	 * 通过反射得到一个cfg
	 * @param cfgClass
	 * @param <Cfg>
	 * @return
	 */
	<Cfg> Cfg generalCfg(JSONObject jsonObject, Class<Cfg> cfgClass) throws Exception {
		Cfg cfg = cfgClass.newInstance();

		Field[] fields = cfgClass.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isPublic(field.getModifiers())
					|| Modifier.isFinal(field.getModifiers())
					|| Modifier.isStatic(field.getModifiers())
					|| Modifier.isTransient(field.getModifiers()))
				continue;

			Object val = convertFieldVal(field, jsonObject);
			field.setAccessible(true);
			field.set(cfg, val);
		}
		return cfg;
	}

	/**
	 * 从JSONObject 去到对应属性名的值
	 * @param field
	 * @param jsonObject
	 * @return
	 */
	private Object convertFieldVal(Field field, JSONObject jsonObject) {
		Class<?> type = field.getType();
		String name = field.getName();

		if (type == Integer.TYPE || type == Integer.class) return jsonObject.getInteger(name);
		if (type == Boolean.TYPE || type == Boolean.class) return jsonObject.getInteger(name) == 1;
		if (type == Long.TYPE || type == Long.class) return jsonObject.getLong(name);
		if (type == Double.TYPE || type == Double.class) return jsonObject.getDouble(name);
		if (type == String.class) return jsonObject.getString(name);

		throw new RuntimeException("not define convert for type ["+type.getName()+"]");
	}
}
