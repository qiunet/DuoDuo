package org.qiunet.utils.system;

import java.lang.management.ManagementFactory;

public class SystemUtil {
	private SystemUtil(){}

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
}
