package org.qiunet.utils.system;

import org.qiunet.utils.exceptions.CustomException;
import sun.misc.VM;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;

public class OSUtil {
	private OSUtil(){}

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

	/**
	 * 获得JVM最大内存
	 *
	 * @return 最大内存
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * 获得JVM已分配内存
	 *
	 * @return 已分配内存
	 */
	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * 获得JVM已分配内存中的剩余空间
	 *
	 * @return 已分配内存中的剩余空间
	 */
	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 获得设定的最大堆外内存
	 * @return
	 */
	public static long getMaxDirectMemory() {
		return VM.maxDirectMemory();
	}

	/**
	 * 获得使用的堆外内存
	 * @return
	 */
	public static long getUsedDirectMemory() {
		BufferPoolMXBean bufferPoolMXBean = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)
				.stream()
				.filter(e -> e.getName().equals("direct"))
				.findFirst()
				.orElseThrow(() -> new CustomException("getUsedDirectMemory throw exception"));
		return bufferPoolMXBean.getMemoryUsed();
	}
	/**
	 * 获得JVM最大可用内存
	 *
	 * @return 最大可用内存
	 */
	public static long getUsableMemory() {
		return getMaxMemory()
				- getTotalMemory()
				+ getFreeMemory();
	}

}
