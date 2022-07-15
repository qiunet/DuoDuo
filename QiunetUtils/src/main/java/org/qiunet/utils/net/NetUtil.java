package org.qiunet.utils.net;

import com.google.common.net.InetAddresses;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.thread.ThreadPoolManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/22 12:04
 **/
public class NetUtil {
	/**
	 * 默认最小端口，1024
	 */
	public static final int PORT_RANGE_MIN = 1024;
	/**
	 * 默认最大端口，65535
	 */
	public static final int PORT_RANGE_MAX = 0xFFFF;
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
		InetAddress inetAddress = InetAddresses.forString(ip);
		return isInnerIp(inetAddress);
	}
	/**
	 * 是否是内网IP
	 * @return
	 */
	public static boolean isInnerIp(InetAddress inetAddress){
		return inetAddress.isSiteLocalAddress() || inetAddress.isLoopbackAddress();
	}

	/***
	 * 得到内网ip v4
	 * @return
	 */
	public static String getInnerIp() {
		return localIpv4s().stream().filter(str -> ! isLocalIp(str)).findFirst().get();
	}
	/**
	 * 是否为有效的端口<br>
	 * 此方法并不检查端口是否被占用
	 *
	 * @param port 端口号
	 * @return 是否有效
	 */
	public static boolean isValidPort(int port) {
		// 有效端口是0～65535
		return port >= 0 && port <= PORT_RANGE_MAX;
	}
	/**
	 * 得到外网IP
	 * @return
	 */
	public static String getPublicIp(){
		LinkedHashSet<InetAddress> inetAddresses = localAddressList(address ->
				! isInnerIp(address)
				&& address instanceof Inet4Address);
		Optional<InetAddress> first = inetAddresses.stream().findFirst();
		String ip = first.map(InetAddress::getHostAddress).orElse(null);
		if (!StringUtil.isEmpty(ip)) {
			// 先读取本地网络配置的
			return ip;
		}

		// 下面的ip都可以获得ip地址.
		// whatismyip.akamai.com
		// ipecho.net/plain
		// v4.ident.me
		// ident.me
		// ip.sb
		String[] IPV4_SERVICES = {
				"http://checkip.amazonaws.com",
				"https://ipv4.icanhazip.com",
				"https://icanhazip.com",
				"https://ipinfo.io/ip"
		};

		List<Callable<String>> callables = Stream.of(IPV4_SERVICES).map(str -> (Callable<String>) () -> {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(str).openStream()))) {
				String result = in.readLine();

				LoggerType.DUODUO.error("Get public ip {} from {}!", result, str);
				if (!NetUtil.isValidIp4(result))  {
					throw new CustomException("ip invalid!");
				}
				return result;
			}
		}).collect(Collectors.toList());

		try {
			String publicIp = ThreadPoolManager.NORMAL.invokeAny(callables);
			LoggerType.DUODUO.error("current public ip: {}", publicIp);
			return publicIp;
		} catch (InterruptedException | ExecutionException e) {
			throw new CustomException("No public ip get!");
		}
	}

	/**
	 * 是否是合格的ip4
	 * @param host
	 * @return
	 */
	public static boolean isValidIp4(String host) {
		String[] strings = StringUtil.split(host, ".");
		if (strings.length != 4) {
			return false;
		}
		for (int i = 0; i < strings.length; i++) {
			if (! StringUtil.isNum(strings[i])) {
				return false;
			}
			int i1 = Integer.parseInt(strings[i]);
			if (i1 < 0 || i1 > 255) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 使用Socket发送数据
	 *
	 * @param host Server主机
	 * @param port Server端口
	 * @param data 数据
	 */
	public static void socketSendData(String host, int port, byte[] data) throws IOException {
		try (SocketChannel channel = SocketChannel.open(InetSocketAddress.createUnresolved(host, port))) {
			channel.write(ByteBuffer.wrap(data));
		}
	}
	/**
	 * 使用udp发送数据
	 *
	 * @param host Server主机
	 * @param port Server端口
	 * @param data 数据
	 */
	public static void udpSendData(String host, int port, byte[] data) throws IOException {
		try (DatagramChannel channel = DatagramChannel.open()){
			channel.send(ByteBuffer.wrap(data), new InetSocketAddress(host, port));
		}
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
		LinkedHashSet<InetAddress> inetAddresses = localAddressList(i -> true);
		return toIpList(inetAddresses).toArray(new String[0]);
	}
	/**
	 * 获得本机的IPv4地址列表<br>
	 * 返回的IP列表有序，按照系统设备顺序
	 *
	 * @return IP地址列表 {@link LinkedHashSet}
	 */
	public static LinkedHashSet<String> localIpv4s() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(t -> t instanceof Inet4Address);
		return toIpList(localAddressList);
	}

	/**
	 * 获得本机的IPv6地址列表<br>
	 * 返回的IP列表有序，按照系统设备顺序
	 *
	 * @return IP地址列表 {@link LinkedHashSet}
	 * @since 4.5.17
	 */
	public static LinkedHashSet<String> localIpv6s() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(t -> t instanceof Inet6Address);
		return toIpList(localAddressList);
	}
	/**
	 * 地址列表转换为IP地址列表
	 *
	 * @param addressList 地址{@link Inet4Address} 列表
	 * @return IP地址字符串列表
	 * @since 4.5.17
	 */
	public static LinkedHashSet<String> toIpList(Set<InetAddress> addressList) {
		final LinkedHashSet<String> ipSet = new LinkedHashSet<>();
		for (InetAddress address : addressList) {
			ipSet.add(address.getHostAddress());
		}

		return ipSet;
	}
	/**
	 * 获取所有满足过滤条件的本地IP地址对象
	 *
	 * @param addressFilter 过滤器，null表示不过滤，获取所有地址
	 * @return 过滤后的地址对象列表
	 * @since 4.5.17
	 */
	public static LinkedHashSet<InetAddress> localAddressList(Predicate<InetAddress> addressFilter) {
		Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		if (networkInterfaces == null) {
			throw new CustomException("Get network interface error!");
		}

		final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if (inetAddress != null && (null == addressFilter || addressFilter.test(inetAddress))) {
					ipSet.add(inetAddress);
				}
			}
		}

		return ipSet;
	}
}
