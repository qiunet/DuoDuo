package org.qiunet.frame.setting;


import org.qiunet.utils.Excel2CfgsUtil;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.encryptAndDecrypt.StrCodecUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

/***
 *
 *
 * qiunet
 * 2019-11-06 14:57
 ***/
public class SettingManager {
	private static final SettingManager instance = new SettingManager();

	private SettingManager(){
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		this.loadSetting();
	}

	public static SettingManager getInstance() {
		return instance;
	}

	private Setting setting;
	public Setting getSetting() {
		return setting;
	}
	/***
	 * 将path 添加到setting里面
	 * @param path
	 * @return 是否成功
	 */
	public boolean addExcelPath(String path) {
		String encryptPath = StrCodecUtil.encrypt(path);
		boolean existInList = CommonUtil.existInList(encryptPath, setting.getExcelPaths());
		if (existInList) {
			setting.getExcelPaths().remove(encryptPath);
		}
		setting.getExcelPaths().addFirst(encryptPath);
		return ! existInList;
	}

	/***
	 * 将path 添加到setting里面
	 * @param path
	 * @return 是否成功
	 */
	public boolean addCfgPath(String path) {
		String encryptPath = StrCodecUtil.encrypt(path);
		boolean existInList = CommonUtil.existInList(encryptPath, setting.getCfgPaths());
		if (existInList) {
			setting.getCfgPaths().remove(encryptPath);
		}
		setting.getCfgPaths().addFirst(encryptPath);
		return ! existInList;
	}

	/***
	 * 删除指定的路径
	 * @param path
	 */
	public void removeCfgPath(String path) {
		String encryptPath = StrCodecUtil.encrypt(path);
		setting.getCfgPaths().remove(encryptPath);
	}

	/***
	 * 返回第一个路径. 默认值
	 * @return
	 */
	public String getFirstCfgPath() {
		if (setting.getCfgPaths().isEmpty()) return null;
		return StrCodecUtil.decrypt(setting.getCfgPaths().getFirst());
	}

	/***
	 * 删除指定的路径
	 * @param path
	 */
	public void removeExcelPath(String path) {
		String encryptPath = StrCodecUtil.encrypt(path);
		setting.getExcelPaths().remove(encryptPath);
	}

	/***
	 * 返回第一个路径. 默认值
	 * @return
	 */
	public String getFirstExcelPath() {
		if (setting.getExcelPaths().isEmpty()) return null;
		return StrCodecUtil.decrypt(setting.getExcelPaths().getFirst());
	}

	/***
	 * 得到真实的 excel路径列表
	 * @return
	 */
	public List<String> getExcelPaths() {
		return setting.getExcelPaths().stream().map(StrCodecUtil::decrypt).collect(Collectors.toList());
	}

	/**
	 * 得到真实的 cfg 路径列表
	 * @return
	 */
	public List<String> getCfgPaths() {
		return setting.getCfgPaths().stream().map(StrCodecUtil::decrypt).collect(Collectors.toList());
	}

	/***
	 * 同步保存
	 */
	public void syncSetting(){
		Excel2CfgsUtil.writeToProjectFile(this.setting);
	}

	/***
	 *  加载setting
	 */
	private void loadSetting(){
		String json = Excel2CfgsUtil.returnPathFromProjectFile();
		if (StringUtil.isEmpty(json)) {
			this.setting = new Setting();
			return;
		}
		this.setting = JsonUtil.getGeneralObject(json, Setting.class);
	}
}
