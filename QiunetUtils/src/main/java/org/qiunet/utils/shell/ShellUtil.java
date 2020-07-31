package org.qiunet.utils.shell;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class ShellUtil {
	private ShellUtil(){}
	/**
	 * 执行系统脚本 主要是linux 带参数
	 * @param shell 数组第一位为命令 后面是参数
	 * @return
	 */
	public static String execShell(String... shell){
		if(! "/".equals(File.separator)) return "";

		shell[0] = shell[0].replace("~", System.getProperty("user.home"));
		StringJoiner sb = new StringJoiner("\n");
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(shell);
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
	       		sb.add("脚本错误输出:");
	       		sb.add(sbError.toString());
	       	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}


}
