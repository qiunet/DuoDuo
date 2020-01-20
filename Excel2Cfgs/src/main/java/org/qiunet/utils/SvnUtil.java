package org.qiunet.utils;

import javafx.application.Platform;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;

import java.io.IOException;
public class SvnUtil {
	private static Logger logger = LoggerType.DUODUO.getLogger();
	private static SystemPropertyUtil.OSType osType = SystemPropertyUtil.getOsName();


	public static void svnEvent(SvnCommand command, String path) {
		Platform.runLater(() -> execSvnCommand(command, path));
	}

	private static void execSvnCommand(SvnCommand command, String path) {
		switch (osType) {
			case WINDOWS:
				ProcessBuilder builder = new ProcessBuilder("TortoiseProc.exe", "/command:"+command.name().toLowerCase()+" /path:\""+path+"\" /closeonend:1");
				try {
					Process process = builder.start();
					System.out.println(process.exitValue()+"=============================");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MAC_OS:
			case LINUX:
				break;
			default:
				break;
		}
	}

	public enum SvnCommand {
		//提交
		COMMIT,
		//更新
		UPDATE,
		// 清理
		CLEANUP,
		//添加
		ADD,;
	}

	public static void main(String[] args) {
		svnEvent(SvnCommand.UPDATE, "C:\\Users\\qiune\\Documents\\excelConfig");
	}
}
