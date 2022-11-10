package org.qiunet.flash.handler.netty.server.param;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.CrossProtocolHeader;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.ServerNodeProtocolHeader;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;
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
public class ServerBootStrapParam {
	/**
	 * 启动的上下文
	 */
	private IStartupContext<? extends IMessageActor<?>> startupContext;
	/**
	 * 可以自定义协议头
	 */
	private IProtocolHeader protocolHeader;
	/***
	 * 客户端每5秒一次心跳
	 * 读超时处理.
	 */
	private int readIdleCheckSeconds = 60 * 5;
	/**
	 * 最大接受的数据长度
	 */
	private int maxReceivedLength = 1024 * 1024;
	/**
	 * http 启动参数
	 */
	private HttpBootstrapParam httpParam = HttpBootstrapParam.DEFAULT_PARAM;
	/**
	 * tcp 启动参数
	 */
	private TcpBootstrapParam tcpParam = TcpBootstrapParam.DEFAULT_PARAM;
	/**
	 * kcp 启动参数
	 */
	private KcpBootstrapParam kcpParam;
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

	private ServerBootStrapParam(String serverName, int port) {
		Preconditions.checkState(port > 1000);
		Preconditions.checkNotNull(serverName);
		this.serverName = serverName;
		this.port = port;
	}

	public static Builder newBuild(String serverName, int port){
		ServerBootStrapParam param = new ServerBootStrapParam(serverName, port);
		return param.new Builder();
	}

	public String getServerName() {
		return serverName;
	}

	public HttpBootstrapParam getHttpParam() {
		return httpParam;
	}

	public TcpBootstrapParam getTcpParam() {
		return tcpParam;
	}

	public KcpBootstrapParam getKcpParam() {
		return kcpParam;
	}

	public IProtocolHeader getProtocolHeader() {
		return protocolHeader;
	}

	public boolean isBanHttpServer() {
		return banHttpServer;
	}

	public IStartupContext<? extends IMessageActor<?>> getStartupContext() {
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
			ServerBootStrapParam.this.protocolHeader = protocolHeader;
			return this;
		}

		public Builder banHttpServer() {
			ServerBootStrapParam.this.banHttpServer = true;
			return this;
		}

		public Builder setStartupContext(IStartupContext<? extends IMessageActor<?>> startupContext) {
			ServerBootStrapParam.this.startupContext = startupContext;
			return this;
		}
		public Builder setReadIdleCheckSeconds(int readIdleCheckSeconds) {
			ServerBootStrapParam.this.readIdleCheckSeconds = readIdleCheckSeconds;
			return this;
		}

		public Builder setHttpBootStrapParam(HttpBootstrapParam param) {
			ServerBootStrapParam.this.httpParam = param;
			return this;
		}

		public Builder setTcpBootStrapParam(TcpBootstrapParam param) {
			ServerBootStrapParam.this.tcpParam = param;
			if (param.isUdpOpen()) {
				return this.setKcpBootStrapParam(param.kcpBootstrapParam);
			}
			return this;
		}

		public Builder setKcpBootStrapParam(KcpBootstrapParam param) {
			ServerBootStrapParam.this.kcpParam = param;
			if (param != null) {
				param.cal(ServerBootStrapParam.this.port);
			}
			return this;
		}

		/**
		 * 需要校验
		 * @return builder
		 */
		public Builder encryption() {
			ServerBootStrapParam.this.encryption = true;
			return this;
		}

		public Builder setMaxReceivedLength(int maxReceivedLength) {
			ServerBootStrapParam.this.maxReceivedLength = maxReceivedLength;
			return this;
		}

		/**
		 * 定制 IProtocolHeader
		 * @return
		 */
		private IProtocolHeader customProtocolHeader() {
			//protocol header 自定义主要是要来限制客户端到游戏服.
			if (ServerNodeManager.getCurrServerType() == ServerType.CROSS && getPort() == ServerNodeManager.getCurrServerInfo().getServerPort()) {
				// 玩法服的server port 必须使用 CrossProtocolHeader.
				return CrossProtocolHeader.instance;
			}

			if (getPort() == ServerNodeManager.getCurrServerInfo().getCrossPort()) {
				// cross port 必须使用 CrossProtocolHeader.
				return CrossProtocolHeader.instance;
			}

			if (getPort() == ServerNodeManager.getCurrServerInfo().getNodePort()) {
				// 服务器之间通讯 必须使用 ServerNodeProtocolHeader.
				return ServerNodeProtocolHeader.instance;
			}

			// 之后有设定. 使用设定的. 否则默认的
			if (protocolHeader != null) {
				return protocolHeader;
			}

			return DefaultProtocolHeader.instance;
		}

		public ServerBootStrapParam build() {
			if (ServerBootStrapParam.this.protocolHeader == null){
				ServerBootStrapParam.this.protocolHeader = customProtocolHeader();
			}
			return ServerBootStrapParam.this;
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
	public static class KcpBootstrapParam {
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
		private Param param = Param.DEFAULT_KCP_PARAM;
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

		private KcpBootstrapParam() {}

		/**
		 * 计算现有的端口等数据
		 * @param port 服务端口
		 */
		public void cal(int port) {
			this.ports = IntStream.range(0, this.portCount).map(i -> i  + portOffset + port).boxed().collect(Collectors.toSet());
			this.udpPortChooser = PollChooserFactory.DEFAULT.newChooser(Lists.newArrayList(ports));
		}

		public static Builder newBuild() {
			return new KcpBootstrapParam().new Builder();
		}

		public PollChooserFactory.PollChooser<Integer> getUdpPortChooser() {
			return udpPortChooser;
		}

		public boolean isDependOnTcpWs() {
			return dependOnTcpWs;
		}

		public Param getParam() {
			return param;
		}

		public Set<Integer> getPorts() {
			return ports;
		}

		public class Builder {

			public Builder setKcpParam(Param param) {
				KcpBootstrapParam.this.param = param;
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
				KcpBootstrapParam.this.portOffset = portOffset;
				KcpBootstrapParam.this.portCount = portCount;
				return this;
			}

			public KcpBootstrapParam build() {
				return KcpBootstrapParam.this;
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
		public record Param(int snd_wnd, int rcv_wnd, int interval, boolean noDelay, int mtu, int fastResend, boolean noCwnd) {
			public static final Param DEFAULT_KCP_PARAM = new Param(512, 512, 20, true, 512, 2, true);
		}
	}

	/**
	 * Tcp 的启动参数
	 */
	public static class TcpBootstrapParam {
		public static final TcpBootstrapParam DEFAULT_PARAM = new TcpBootstrapParam();
		/**
		 * 在端口上开放udp
		 */
		private KcpBootstrapParam kcpBootstrapParam;
		private TcpBootstrapParam(){}

		public boolean isUdpOpen() {
			return kcpBootstrapParam != null;
		}

		public KcpBootstrapParam getKcpBootstrapParam() {
			return kcpBootstrapParam;
		}

		public static Builder newBuild() {
			return new TcpBootstrapParam().new Builder();
		}

		public class Builder {

			public Builder setUdpOpen(KcpBootstrapParam param) {
				TcpBootstrapParam.this.kcpBootstrapParam = param;
				param.dependOnTcpWs = true;
				return this;
			}

			public TcpBootstrapParam build() {
				return TcpBootstrapParam.this;
			}
		}

	}
	/**
	 * Http 的参数
	 */
	public static class HttpBootstrapParam {
		public static final HttpBootstrapParam DEFAULT_PARAM = new HttpBootstrapParam();
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

		private HttpBootstrapParam() {}

		public static HttpBootstrapParam.Builder newBuild() {
			return new HttpBootstrapParam().new Builder();
		}

		public String getWebsocketPath() {
			return websocketPath;
		}

		public String getGameURIPath() {
			return gameURIPath;
		}

		public class Builder {
			private Builder() {}

			public Builder setWebsocketPath(String websocketPath) {
				HttpBootstrapParam.this.websocketPath = websocketPath;
				return this;
			}

			public Builder setGameURIPath(String gameURIPath) {
				HttpBootstrapParam.this.gameURIPath = gameURIPath;
				return this;
			}

			public HttpBootstrapParam build() {
				return HttpBootstrapParam.this;
			}
		}
	}
}
