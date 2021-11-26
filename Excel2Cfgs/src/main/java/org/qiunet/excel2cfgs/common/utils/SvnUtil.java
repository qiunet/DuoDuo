package org.qiunet.excel2cfgs.common.utils;

import org.qiunet.excel2cfgs.swing.SwingUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

/***
 * svn 处理
 * @author qiunet
 */
public class SvnUtil {
	/**svn正操作中 后期可以做监听, 告知按钮不可被操作.*/
	private static boolean processing = false;

	private static final boolean windows = Excel2CfgsUtil.isWindows();


	public static void svnEvent(SvnCommand command, String path) {
		if (processing) {
			return;
		}

		if (windows) {
			try {
				Runtime.getRuntime().exec("TortoiseProc.exe /command:" + command.name().toLowerCase() + " /path:\"" + path + "\" /closeonend:1");
			} catch (IOException e) {
				SwingUtil.alterError(e.getMessage());
			}
		}else {
			if (command == SvnCommand.COMMIT && needAdd(path)) {
				handlerMacOrlinux(SvnCommand.ADD, path);
			}
			handlerMacOrlinux(command, path);
		}
	}
	private static boolean needAdd(String path){
		return false;
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
			SwingUtil.alterError("执行命令报错"+e.getMessage());
			e.printStackTrace();
		}
		if (process == null) {
			return;
		}
		processing = true;
		boolean haveProcessMsg = false;
		try (
			InputStreamReader ir=new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
			LineNumberReader input = new LineNumberReader (ir);
			InputStreamReader irError = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
			LineNumberReader inputError = new LineNumberReader(irError)
		){
			String line;
			while ((line = input.readLine ()) != null){
				SwingUtil.sendMsgToConsole(line, true);
				haveProcessMsg = true;
			}
			while((line = inputError.readLine())!=null){
				SwingUtil.sendMsgToConsole(line, true);
				haveProcessMsg = true;
			}
			process.waitFor();
		} catch (Exception e) {
			SwingUtil.alterError("异常: "+e.getMessage());
			e.printStackTrace();
		}finally {
			processing = false;
		}
		if (! haveProcessMsg) {
			SwingUtil.sendMsgToConsole(process.exitValue() == 0 ? "执行成功": "执行失败", true);
		}
	}


	public enum SvnCommand {
		//提交
		COMMIT,
		//更新
		UPDATE,
		// 得锁
		LOCK,
		// 解锁
		UNLOCK,
		// 清理
		CLEANUP,
		//添加
		ADD,
	}
}
