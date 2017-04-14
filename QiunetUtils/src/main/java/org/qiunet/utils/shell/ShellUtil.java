package org.qiunet.utils.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

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

		StringBuilder sb = new StringBuilder();

		Process process = null;
		try {
			process = Runtime.getRuntime().exec(shell);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		InputStreamReader ir=new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader (ir);
		String line;
		try {
			while ((line = input.readLine ()) != null){
				sb.append(line).append("\n");
			}

			ir = new InputStreamReader(process.getErrorStream());
			input = new LineNumberReader(ir);
	       	StringBuilder sbError = new StringBuilder();
			while((line=input.readLine())!=null){
				sbError.append(line).append("\n");
			}
	       	if(sbError.length() > 0){
	       		sb.append("脚本错误输出:\n");
	       		sb.append(sbError);
	       	}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(ir != null)
				try {
					ir.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return sb.toString();
	}


}
