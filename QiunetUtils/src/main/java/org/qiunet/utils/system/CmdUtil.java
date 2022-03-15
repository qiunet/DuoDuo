package org.qiunet.utils.system;

import com.google.common.base.Preconditions;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.StringJoiner;

public class CmdUtil {
	private CmdUtil(){}
	/**
	 * 执行系统脚本 主要是linux 带参数
	 * @param commandPath 命令路径 或者命令名
	 * @return
	 */
	public static String exec(Path commandPath, String ... args){
		return exec(commandPath.toString(), args);
	}

	public static String exec(String commandPath, String ... args){
		String command = commandPath;
		command = command.replace("~", System.getProperty("user.home"));
		StringJoiner sb = new StringJoiner("\n");
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(StringUtil.arraysToString(args, command+" ", "", " "));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Preconditions.checkNotNull(process, "process is null");
		try (
			InputStreamReader ir=new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
			LineNumberReader input = new LineNumberReader (ir);
			InputStreamReader irError = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
			LineNumberReader inputError = new LineNumberReader(irError)
		){
			String line;
			while ((line = input.readLine ()) != null){
				sb.add(line);
			}

	       	StringJoiner sbError = new StringJoiner("\n");
			while((line = inputError.readLine())!=null){
				sbError.add(line);
			}
	       	if(sbError.length() > 0){
	       		throw new CustomException(sbError.toString());
	       	}
		} catch (Exception e) {
			throw new CustomException(e, "执行异常");
		}
		return sb.toString();
	}


}
