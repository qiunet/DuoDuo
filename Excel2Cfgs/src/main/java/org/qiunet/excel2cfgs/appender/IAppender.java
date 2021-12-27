package org.qiunet.excel2cfgs.appender;


import org.qiunet.excel2cfgs.common.enums.RoleType;
import org.qiunet.excel2cfgs.common.utils.StringUtil;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.SwingUtil;

import java.io.File;
import java.util.Arrays;

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
		RoleType roleType = SettingManager.getInstance().getSetting().getRoleType();
		String baseCfgPath = SettingManager.getInstance().getFirstCfgPath();
		String[] strings = sheetName.split("\\.");
		boolean justServer = Arrays.stream(strings).limit(Math.max(0, strings.length - 1)).anyMatch("s"::equals);
		boolean justClient = Arrays.stream(strings).limit(Math.max(0, strings.length - 1)).anyMatch("c"::equals);
		boolean all = !justClient && !justServer;
		if (justClient || justServer) {
			sheetName = strings[strings.length - 1];
		}

		if (roleType != RoleType.SCHEMER && StringUtil.isEmpty(baseCfgPath)) {
			SwingUtil.alterError("服务端客户端需要设定导出的项目路径");
			return;
		}

		switch (roleType) {
			case SERVER:
				if (all || justServer) {
					this.createCfgFile(sheetName, roleType, baseCfgPath, attachable);
				}
				break;
			case CLENTER:
				if (all || justClient) {
					this.createCfgFile(sheetName, roleType, baseCfgPath, attachable);
				}
				break;
			case SCHEMER:
				if (all || justServer) {
					this.createCfgFile(sheetName, RoleType.SERVER, getServerOutputPath(), attachable);
				}
				if (all || justClient) {
					this.createCfgFile(sheetName, RoleType.CLENTER, getClientOutputPath(), attachable);
				}
				break;
			default:
				break;
		}
	 }

	/***
	 * 输出配置文件
	 * @param roleType 角色
	 * @param outPath 输出路径
	 * @param attachable
	 */
	void createCfgFile(String sheetName, RoleType roleType, String outPath, AppenderAttachable attachable);
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
