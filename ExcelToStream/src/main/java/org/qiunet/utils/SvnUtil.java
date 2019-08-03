package org.qiunet.utils;

import javafx.scene.control.Alert;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;

import java.io.*;

public class SvnUtil {
	private static Logger logger = LoggerType.DUODUO.getLogger();
	private static OSType osType = OSType.getOSType(SystemPropertyUtil.getOsName());

	public static String svnEvent(SvnCommand command, String path, boolean close) {
		if (StringUtil.isEmpty(path))
			return "路径不能为null!";
		String shell = splicing(command, path, close);
		return execSvn(shell);
	}

	private static String splicing(SvnCommand command, String path, boolean close) {
		StringBuilder sb = new StringBuilder();
		switch (osType) {
			case WINDOWS: {
				sb.append("TortoiseProc.exe /command:").append(command.name()).append(" /path:").append(path);
				if (close)
					sb.append(" /closeonend:2");
				break;
			}
			case MAC_OS:
			case LINUX:{
				sb.append("svn ").append(command.name()).append(" ").append(path);
				break;
			}

		}
		return sb.toString();
	}

	public enum SvnCommand {
		commit,//提交
		update,//更新
		;
	}

	public enum OSType {
		MAC_OS,
		WINDOWS,
		LINUX,;

		public boolean is(OSType type) {
			return this == type;
		}

		public static OSType getOSType(String osName) {
			if (osName.startsWith("Mac OS")) {
				return OSType.MAC_OS;
			} else if (osName.startsWith("Windows")) {
				return OSType.WINDOWS;
			} else {
				return OSType.LINUX;
			}
		}
	}


	public static String execSvn(String svn) {
		try {
			Process process = Runtime.getRuntime().exec(svn);
			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");
			new Thread(errorGobbler).start();
			new Thread(outputGobbler).start();
			process.waitFor();
			process.destroy();
			return "执行svn[" + svn + "]命令成功！\n";
		} catch (Exception e) {
			String error="执行svn命令出错！" + svn + "\n" + e.getMessage()+"\n";
			FxUIUtil.openAlert(Alert.AlertType.ERROR, error, "错误");
			return error;
		}

	}


	private static class StreamGobbler implements Runnable {
		InputStream is;
		String type;

		public StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		@Override
		public void run() {
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					if (type.equals("Error")) {
						sb.append(line);
					}
				}
				if (!StringUtil.isEmpty(sb.toString())) {
					logger.error(sb.toString());
				}
			} catch (Exception ioe) {
				logger.error("异常！", ioe);
			} finally {
				try {
					if (br != null) br.close();
					if (isr != null) isr.close();
					if (is != null) is.close();
				} catch (IOException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}

		}
	}
}
