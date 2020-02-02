package org.qiunet.excel2cfgs.utils;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringJoiner;

public class SvnUtil {
	/**svn正操作中 后期可以做监听, 告知按钮不可被操作.*/
	private static boolean processing = false;

	private static Logger logger = LoggerType.DUODUO.getLogger();

	private static SystemPropertyUtil.OSType osType = SystemPropertyUtil.getOsName();


	public static void svnEvent(SvnCommand command, String path) {
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
				if (command == SvnCommand.COMMIT) {
					handlerMacOrlinux(SvnCommand.ADD, path);
				}
				handlerMacOrlinux(command, path);
				break;
			default:
				break;
		}
	}

	/**
	 * 处理 mac 和 linux 的svn命令
	 * 内容太长
	 * @param command
	 * @param path
	 */
	private static void handlerMacOrlinux(SvnCommand command, String path) {
		StringJoiner sb = new StringJoiner(" ");
		sb.add("svn").add(command.name().toLowerCase()).add(path);
		if (command == SvnCommand.COMMIT) {
			sb.add("-m").add("''");
		}
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(sb.toString());
		} catch (IOException e) {
			FxUIUtil.alterError("执行命令报错"+e.getMessage());
			e.printStackTrace();
		}
		if (process == null) {
			return;
		}
		processing = true;
		boolean haveProcessMsg = false;
		try (
			InputStreamReader ir=new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader (ir);
			InputStreamReader irError = new InputStreamReader(process.getErrorStream());
			LineNumberReader inputError = new LineNumberReader(irError)
		){
			String line;
			while ((line = input.readLine ()) != null){
				FxUIUtil.sendMsgToTextInput(line, true);
				haveProcessMsg = true;
			}
			while((line = inputError.readLine())!=null){
				FxUIUtil.sendMsgToTextInput(line, true);
				haveProcessMsg = true;
			}
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			processing = false;
		}
		if (! haveProcessMsg) {
			FxUIUtil.sendMsgToTextInput(process.exitValue() == 0 ? "执行成功": "执行失败", true);
		}
	}

	/**
	 * svn 操作状态
	 * @return
	 */
	public static boolean isProcessing() {
		return processing;
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
}
