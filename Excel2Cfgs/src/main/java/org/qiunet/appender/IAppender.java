package org.qiunet.appender;


import javafx.scene.control.Alert;
import org.qiunet.frame.enums.RoleType;
import org.qiunet.frame.setting.SettingManager;
import org.qiunet.utils.FxUIUtil;
import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.util.List;

/**
 * 一个文件的处理流程
 * @author  qiunet.
 * 17/10/30
 */
public interface IAppender {
	/***
	 * 一个sheet结束
	 * @param sheetName
	 */
	 default void sheetOver(String sheetName, AppenderAttachable attachable){
		String roleType = SettingManager.getInstance().getSetting().getRoleType();
		String baseCfgPath = SettingManager.getInstance().getFirstCfgPath();
		boolean clienterContinue = sheetName.startsWith("_");
		if (clienterContinue) {
			sheetName = sheetName.substring(1);
		}

		if (! roleType.equals(RoleType.SCHEMER) && StringUtil.isEmpty(baseCfgPath)) {
			FxUIUtil.openAlert(Alert.AlertType.ERROR, "服务端客户端需要设定导出的项目路径", "错误");
			return;
		}

		switch (roleType) {
			case RoleType.SERVER:
				this.createCfgFile(sheetName,true, baseCfgPath, attachable);
				break;
			case RoleType.CLENTER:
				if (! clienterContinue) {
					this.createCfgFile(sheetName,false, baseCfgPath, attachable);
				}
				break;
			case RoleType.SCHEMER:
				this.createCfgFile(sheetName,true, getServerOutputPath(), attachable);
				if (! clienterContinue) {
					this.createCfgFile(sheetName,false, getClientOutputPath(), attachable);
				}
				break;
			default:
				break;
		}
	 }

	/***
	 * 输出配置文件
	 * @param server 是否输出模式为服务端. 否则为客户端
	 * @param outPath 输出路径
	 * @param attachable
	 */
	void createCfgFile(String sheetName, boolean server, String outPath, AppenderAttachable attachable);
	/***
	 * 文件结束
	 */
	 void fileOver();

	/**
	 * 获得输出路径
	 * @return
	 */
	default String getServerOutputPath() {
		return SettingManager.getInstance().getFirstExcelPath()
			+ File.separator + "."
			+ this.name().toLowerCase() + ".config/server";
	}
	/**
	 * 获得输出路径
	 * @return
	 */
	default String getClientOutputPath() {
		return SettingManager.getInstance().getFirstExcelPath()
			+ File.separator + "."
			+ this.name().toLowerCase() + ".config/client";
	}

	/***
	 * append 名称
	 * @return
	 */
	String name();
}
