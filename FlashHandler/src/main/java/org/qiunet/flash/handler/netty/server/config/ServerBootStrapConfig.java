package org.qiunet.flash.handler.netty.server.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.netty.server.config.adapter.DefaultStartupContext;
import org.qiunet.flash.handler.netty.server.config.adapter.IStartupContext;
import org.qiunet.utils.math.PollChooserFactory;
import org.qiunet.utils.system.OSUtil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * 启动参数设置
 *
 * @author qiunet
 * 2022/10/27 11:27
 */
public class ServerBootStrapConfig {
	/**
	 * 启动的上下文
	 */
	private IStartupContext startupContext = new DefaultStartupContext();
	/**
	 * 可以自定义协议头
	 */
	private IProtocolHeader protocolHeader = DefaultProtocolHeader.instance;
	/***
	 * 客户端每5秒一次心跳
	 * 读超时处理.
	 */
	private int readIdleCheckSeconds = 30;
	/**
	 * 最大接受的数据长度
	 */
	private int maxReceivedLength = 1024 * 1024;
	/**
	 * http 启动参数
	 */
	private HttpBootstrapConfig httpConfig = HttpBootstrapConfig.DEFAULT_PARAM;
	/**
	 * tcp 启动参数
	 */
	private TcpBootstrapConfig tcpConfig = TcpBootstrapConfig.DEFAULT_CONFIG;
	/**
	 * kcp 启动参数
	 */
	private KcpBootstrapConfig kcpConfig;
	/**
	 * 是否检验crc
	 * 一般测试时候使用
	 */
	private boolean encryption;
	/**
	 * 禁用Http Server
	 * 实在用不到就禁掉, 免得通过不是自己定义的端口请求到不该请求的协议.
	 */
	private boolean banHttpServer;
	/**
	 * 当前启动的服务名
	 */
	private final String serverName;
	/***
	 * 接收端口
	 */
	private final int port;

	private ServerBootStrapConfig(String serverName, int port) {
		Preconditions.checkState(port != ServerNodeManager.getCurrServerInfo().getNodePort());
		Preconditions.checkState(port > 1000);
		Preconditions.checkNotNull(serverName);
		this.serverName = serverName;
		this.port = port;
	}

	public static Builder newBuild(String serverName, int port){
		ServerBootStrapConfig config = new ServerBootStrapConfig(serverName, port);
		return config.new Builder();
	}

	public String getServerName() {
		return serverName;
	}

	public HttpBootstrapConfig getHttpBootstrapConfig() {
		return httpConfig;
	}

	public TcpBootstrapConfig getTcpBootstrapConfig() {
		return tcpConfig;
	}

	public KcpBootstrapConfig getKcpBootstrapConfig() {
		return kcpConfig;
	}

	public IProtocolHeader getProtocolHeader() {
		return protocolHeader;
	}

	public boolean isBanHttpServer() {
		return banHttpServer;
	}

	public IStartupContext getStartupContext() {
		return startupContext;
	}

	public int getPort() {
		return port;
	}

	public int getReadIdleCheckSeconds() {
		return readIdleCheckSeconds;
	}

	public boolean isEncryption() {
		return encryption;
	}

	public int getMaxReceivedLength() {
		return maxReceivedLength;
	}

	public class Builder {
		private Builder() {}

		public Builder setProtocolHeader(IProtocolHeader protocolHeader) {
			ServerBootStrapConfig.this.protocolHeader = protocolHeader;
			return this;
		}

		public Builder banHttpServer() {
			ServerBootStrapConfig.this.banHttpServer = true;
			return this;
		}

		public Builder setStartupContext(IStartupContext startupContext) {
			ServerBootStrapConfig.this.startupContext = startupContext;
			return this;
		}
		public Builder setReadIdleCheckSeconds(int readIdleCheckSeconds) {
			ServerBootStrapConfig.this.readIdleCheckSeconds = readIdleCheckSeconds;
			return this;
		}

		public Builder setHttpBootStrapConfig(HttpBootstrapConfig config) {
			ServerBootStrapConfig.this.httpConfig = config;
			return this;
		}

		public Builder setTcpBootStrapConfig(TcpBootstrapConfig config) {
			ServerBootStrapConfig.this.tcpConfig = config;
			if (config.isUdpOpen()) {
				return this.setKcpBootStrapConfig(config.kcpBootstrapConfig);
			}
			return this;
		}

		public Builder setKcpBootStrapConfig(KcpBootstrapConfig config) {
			ServerBootStrapConfig.this.kcpConfig = config;
			if (config != null) {
				config.cal(ServerBootStrapConfig.this.port);
			}
			return this;
		}

		/**
		 * 需要校验
		 * @return builder
		 */
		public Builder encryption() {
			ServerBootStrapConfig.this.encryption = true;
			return this;
		}

		public Builder setMaxReceivedLength(int maxReceivedLength) {
			ServerBootStrapConfig.this.maxReceivedLength = maxReceivedLength;
			return this;
		}

		public ServerBootStrapConfig build() {
			return ServerBootStrapConfig.this;
		}
	}

	/**
	 *  * 使用引导类 设置参数 建造者模式
	 *  * KCP参数
	 *  * conv 会话ID
	 *  * mtu 最大传输单元
	 *  * mss 最大分片大小
	 *  * state 连接状态（0xFFFFFFFF表示断开连接）
	 *  * snd_una 第一个未确认的包
	 *  * snd_nxt 待发送包的序号
	 *  * rcv_nxt 待接收消息序号
	 *  * ssthresh 拥塞窗口阈值
	 *  * rx_rttvar ack接收rtt浮动值
	 *  * rx_srtt ack接收rtt静态值
	 *  * rx_rto 由ack接收延迟计算出来的复原时间
	 *  * rx_minrto 最小复原时间
	 *  * snd_wnd 发送窗口大小
	 *  * rcv_wnd 接收窗口大小
	 *  * rmt_wnd, 远端接收窗口大小
	 *  * cwnd, 拥塞窗口大小
	 *  * probe 探查变量，IKCP_ASK_TELL表示告知远端窗口大小。IKCP_ASK_SEND表示请求远端告知窗口大小
	 *  * interval 内部flush刷新间隔
	 *  * ts_flush 下次flush刷新时间戳
	 *  * nodelay 是否启动无延迟模式
	 *  * updated 是否调用过update函数的标识
	 *  * ts_probe, 下次探查窗口的时间戳
	 *  * probe_wait 探查窗口需要等待的时间
	 *  * dead_link 最大重传次数
	 *  * incr 可发送的最大数据量
	 *  *
	 *  * fastresend 触发快速重传的重复ack个数
	 *  * nocwnd 取消拥塞控制
	 *  * stream 是否采用流传输模式
	 *  *
	 *  * snd_queue 发送消息的队列
	 *  * rcv_queue 接收消息的队列
	 *  * snd_buf 发送消息的缓存
	 *  * rcv_buf 接收消息的缓存
	 *  * acklist 待发送的ack列表
	 *  * buffer 存储消息字节流的内存
	 *  * output udp发送消息的回调函数
	 */
	public static class KcpBootstrapConfig {
		/**
		 * 选择器
		 */
		private PollChooserFactory.PollChooser<Integer> udpPortChooser;
		/**
		 * kcp需要监听多个端口
		 * 需要的端口数量
		 */
		private int portCount = OSUtil.isLinux() ? OSUtil.availableProcessors() : 1;
		/**
		 * 其它参数
		 */
		private KcpParam kcpParam = KcpParam.DEFAULT_KCP_PARAM;
		/**
		 * 端口的偏移
		 */
		private int portOffset;
		/**
		 * 依赖tcp ws
		 */
		private boolean dependOnTcpWs;
		/**
		 * kcp 需要开通的多个端口
		 */
		private Set<Integer> ports;

		private KcpBootstrapConfig() {}

		/**
		 * 计算现有的端口等数据
		 * @param port 服务端口
		 */
		public void cal(int port) {
			this.ports = IntStream.range(0, this.portCount).map(i -> i  + portOffset + port).boxed().collect(Collectors.toSet());
			this.udpPortChooser = PollChooserFactory.DEFAULT.newChooser(Lists.newArrayList(ports));
		}

		public static Builder newBuild() {
			return new KcpBootstrapConfig().new Builder();
		}

		public PollChooserFactory.PollChooser<Integer> getUdpPortChooser() {
			return udpPortChooser;
		}

		public boolean isDependOnTcpWs() {
			return dependOnTcpWs;
		}

		public KcpParam getKcpParam() {
			return kcpParam;
		}

		public Set<Integer> getPorts() {
			return ports;
		}

		public class Builder {

			public Builder setKcpParam(KcpParam kcpParameter) {
				KcpBootstrapConfig.this.kcpParam = kcpParameter;
				return this;
			}

			/**
			 * 基于ServerBootstrap.port的数量
			 * @param portCount 数量
			 * @return
			 */
			public Builder setPortCount(int portCount) {
				return this.setPortCount(100, portCount);
			}
			/**
			 * 基于ServerBootstrap.port的数量
			 * @param portOffset 端口偏移值
			 * @param portCount 数量
			 * @return
			 */
			public Builder setPortCount(int portOffset, int portCount) {
				Preconditions.checkState(portOffset >= 0);
				Preconditions.checkState(portCount > 0);
				KcpBootstrapConfig.this.portOffset = portOffset;
				KcpBootstrapConfig.this.portCount = portCount;
				return this;
			}

			public KcpBootstrapConfig build() {
				return KcpBootstrapConfig.this;
			}

		}

		/**
		 * @param snd_wnd    发送窗口大小.
		 *                   响应客户端的接收窗大小也要高于该值.
		 * @param rcv_wnd    接收窗大小. 要大于客户端的发送窗大小
		 * @param interval   间隔. 内部flush刷新间隔
		 * @param noDelay    无延迟
		 * @param mtu        传输单元大小.
		 * @param fastResend 跳过几次后, 快速重传
		 * @param noCwnd     取消拥塞控制
		 */
		public record KcpParam(int snd_wnd, int rcv_wnd, int interval, boolean noDelay, int mtu, int fastResend, boolean noCwnd) {
			public static final KcpParam DEFAULT_KCP_PARAM = new KcpParam(256, 256, 20, true, 512, 2, true);
		}
	}

	/**
	 * Tcp 的启动参数
	 */
	public static class TcpBootstrapConfig {
		public static final TcpBootstrapConfig DEFAULT_CONFIG = new TcpBootstrapConfig();
		/**
		 * 在端口上开放udp
		 */
		private KcpBootstrapConfig kcpBootstrapConfig;
		private TcpBootstrapConfig(){}

		public boolean isUdpOpen() {
			return kcpBootstrapConfig != null;
		}

		public KcpBootstrapConfig getKcpBootstrapConfig() {
			return kcpBootstrapConfig;
		}

		public static Builder newBuild() {
			return new TcpBootstrapConfig().new Builder();
		}

		public class Builder {

			public Builder setUdpOpen(KcpBootstrapConfig config) {
				TcpBootstrapConfig.this.kcpBootstrapConfig = config;
				config.dependOnTcpWs = true;
				return this;
			}

			public TcpBootstrapConfig build() {
				return TcpBootstrapConfig.this;
			}
		}

	}
	/**
	 * Http 的参数
	 */
	public static class HttpBootstrapConfig {
		public static final HttpBootstrapConfig DEFAULT_PARAM = new HttpBootstrapConfig();
		/***
		 * 升级websocket的路径 一般 /ws 没有websocket 需求. 不需要设定.
		 * 根据这个参数判断是不是
		 */
		private String websocketPath = "/ws";
		/**
		 * 游戏的uriPath
		 * http://localhost:8080/f?a=b&c=d 后面的/f
		 */
		private String gameURIPath = "/f";
		/**
		 * uri 后缀
		 */
		private String uriPostfix = ".do";

		private HttpBootstrapConfig() {}

		public static HttpBootstrapConfig.Builder newBuild() {
			return new HttpBootstrapConfig().new Builder();
		}

		public String getWebsocketPath() {
			return websocketPath;
		}

		public String getGameURIPath() {
			return gameURIPath;
		}

		public String getUriPostfix() {
			return uriPostfix;
		}

		public class Builder {
			private Builder() {}

			public Builder setWebsocketPath(String websocketPath) {
				HttpBootstrapConfig.this.websocketPath = websocketPath;
				return this;
			}

			public Builder setUriPostfix(String uriPostfix) {
				HttpBootstrapConfig.this.uriPostfix = uriPostfix;
				return this;
			}

			public Builder setGameURIPath(String gameURIPath) {
				HttpBootstrapConfig.this.gameURIPath = gameURIPath;
				return this;
			}

			public HttpBootstrapConfig build() {
				return HttpBootstrapConfig.this;
			}
		}
	}
}
