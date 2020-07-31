package org.qiunet.utils.net;

import org.qiunet.utils.common.CommonUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/22 12:04
 **/
public class NetUtil {
	//合起来写
	private static final Pattern pattern = Pattern.compile("((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
			+ "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
			+ "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})");


	/**
	 * 是否是本机ip
	 *
	 * 本机也返回true
	 * @return
	 */
	public static boolean isLocalIp(String ip){
		return CommonUtil.existInList(ip, "localhost", "0:0:0:0:0:0:0:1", "127.0.0.1");
	}

	/**
	 * 是否是内网IP
	 * @return
	 */
	public static boolean isInnerIp(String ip){
		Matcher match = pattern.matcher(ip);
		return match.find();
	}

	/***
	 * 得到内网ip
	 * @return
	 */
	public static String getInnerIp() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address != null ? address.getHostAddress() : null;
	}
	/***
	 * 得到主机名
	 * @return
	 */
	public static String getLocalHostName() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address != null ? address.getHostName() : null;
	}

	/**
	 * 获得本机所有的ip
	 * @return
	 */
	public static String[] getAllInnerIp(){
		String localHostName = getLocalHostName();
		String [] ret = null;
		try {
			InetAddress[] inetAddresses = InetAddress.getAllByName(localHostName);
			ret = new String[inetAddresses.length];
			for (int i = 0; i < inetAddresses.length; i++) {
				ret[i] = inetAddresses[i].getHostAddress();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
