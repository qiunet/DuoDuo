package org.qiunet.utils.system;

import java.lang.management.ManagementFactory;

public class OSUtil {
	private OSUtil(){}

	/***
	 * 得到当前机器的内存数byte
	 * @return
	 */
	public static long freeMemory(){
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 有效的cpu核数
	 * @return
	 */
	public static int availableProcessors(){
		return Runtime.getRuntime().availableProcessors();
	}

	/***
	 * 得到当前进程的进程ID
	 * @return
	 */
	public static int pid(){
		return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
	}

	/**
	 * 系统是否是windows
	 * @return
	 */
	public static boolean isWindows() {
		return SystemPropertyUtil.getOsName() == SystemPropertyUtil.OSType.WINDOWS;
	}

	/**
	 * 系统是否是linux
	 * @return
	 */
	public static boolean isLinux() {
		return SystemPropertyUtil.getOsName() == SystemPropertyUtil.OSType.LINUX;
	}
	/**
	 * 系统是否是mac
	 * @return
	 */
	public static boolean isMac() {
		return SystemPropertyUtil.getOsName() == SystemPropertyUtil.OSType.MAC_OS;
	}
}
