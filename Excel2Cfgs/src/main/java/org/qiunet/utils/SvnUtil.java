package org.qiunet.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;

import java.io.*;

public class SvnUtil {
	private static Logger logger = LoggerType.DUODUO.getLogger();
	private static OSType osType = OSType.getOSType(SystemPropertyUtil.getOsName());

	public static void svnEvent(SvnCommand command, String path, boolean close, TextInputControl input,String log) {
		if (StringUtil.isEmpty(path)) {
			FxUIUtil.sendMsgToTextInput(input, "路径不能为null!\n", true);
			return;
		}
		if (!OSType.WINDOWS.is(osType) && SvnCommand.commit == command) {
			execSvn(splicing(SvnCommand.add, path, close,log), input);
		}
		String shell = splicing(command, path, close,log);
		execSvn(shell, input);
	}

	private static String splicing(SvnCommand command, String path, boolean close,String log) {
		StringBuilder sb = new StringBuilder();
		switch (osType) {
			case WINDOWS: {
				sb.append("TortoiseProc.exe /command:").append(command.name()).append(" /path:").append(path);
				if (close)
					sb.append(" /closeonend:2");
				break;
			}
			case MAC_OS:
			case LINUX: {
				sb.append("svn ").append(command.name()).append(" ").append(path);
				if (SvnCommand.add == command) sb.append(" --no-ignore --force ");
				if (SvnCommand.commit == command) sb.append(" -m ").append(log);
				break;
			}

		}
		return sb.toString();
	}

	public enum SvnCommand {
		commit,//提交
		update,//更新
		add,;
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


	public static String execSvn(String svn, TextInputControl input) {
		try {
			Process process = Runtime.getRuntime().exec(svn);
			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error", input);
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output", input);
			new Thread(errorGobbler).start();
			new Thread(outputGobbler).start();
			process.waitFor();
			process.destroy();
			return "执行svn[" + svn + "]命令成功！\n";
		} catch (Exception e) {
			String error = "执行svn命令出错！" + svn + "\n" + e.getMessage() + "\n";
			FxUIUtil.openAlert(Alert.AlertType.ERROR, error, "错误");
			return error;
		}

	}


	private static class StreamGobbler implements Runnable {
		InputStream is;
		String type;
		TextInputControl input;

		public StreamGobbler(InputStream is, String type, TextInputControl input) {
			this.is = is;
			this.type = type;
			this.input = input;
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
						FxUIUtil.sendMsgToTextInput(input, line, true);
					} else {
						FxUIUtil.sendMsgToTextInput(input, line, true);
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
