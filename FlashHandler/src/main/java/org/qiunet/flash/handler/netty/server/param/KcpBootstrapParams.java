package org.qiunet.flash.handler.netty.server.param;

import com.google.common.collect.ImmutableSet;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Set;

/**
 * 使用引导类 设置参数 建造者模式
 * KCP参数
 * conv 会话ID
 * mtu 最大传输单元
 * mss 最大分片大小
 * state 连接状态（0xFFFFFFFF表示断开连接）
 * snd_una 第一个未确认的包
 * snd_nxt 待发送包的序号
 * rcv_nxt 待接收消息序号
 * ssthresh 拥塞窗口阈值
 * rx_rttvar ack接收rtt浮动值
 * rx_srtt ack接收rtt静态值
 * rx_rto 由ack接收延迟计算出来的复原时间
 * rx_minrto 最小复原时间
 * snd_wnd 发送窗口大小
 * rcv_wnd 接收窗口大小
 * rmt_wnd, 远端接收窗口大小
 * cwnd, 拥塞窗口大小
 * probe 探查变量，IKCP_ASK_TELL表示告知远端窗口大小。IKCP_ASK_SEND表示请求远端告知窗口大小
 * interval 内部flush刷新间隔
 * ts_flush 下次flush刷新时间戳
 * nodelay 是否启动无延迟模式
 * updated 是否调用过update函数的标识
 * ts_probe, 下次探查窗口的时间戳
 * probe_wait 探查窗口需要等待的时间
 * dead_link 最大重传次数
 * incr 可发送的最大数据量
 *
 * fastresend 触发快速重传的重复ack个数
 * nocwnd 取消拥塞控制
 * stream 是否采用流传输模式
 *
 * snd_queue 发送消息的队列
 * rcv_queue 接收消息的队列
 * snd_buf 发送消息的缓存
 * rcv_buf 接收消息的缓存
 * acklist 待发送的ack列表
 * buffer 存储消息字节流的内存
 * output udp发送消息的回调函数
 *
 * Created by qiunet.
 * 17/7/19
 */
public final class KcpBootstrapParams extends AbstractBootstrapParam {
	/**
	 * 依赖tcp  ws
	 * 如果依赖tcp ws
	 * playerActor就是从鉴权协议读取映射
	 */
	private boolean dependOnTcpWs;
	/**
	 * 监听的多端口
	 */
	private Set<Integer> ports;
	/**
	 * 放开udp
	 */
	private KcpParam kcpParam = KcpParam.DEFAULT_KCP_PARAM;

	private KcpBootstrapParams(){}

	public boolean isDependOnTcpWs() {
		return dependOnTcpWs;
	}

	public KcpParam getKcpParam() {
		return kcpParam;
	}

	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		KcpBootstrapParams params = new KcpBootstrapParams();
		return params.new Builder();
	}

	@Override
	public boolean isEncryption() {
		return super.isEncryption();
	}

	@Override
	public int getPort() {
		throw new CustomException("not support!");
	}

	public Set<Integer> getPorts() {
		return ports;
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<KcpBootstrapParams, Builder> {

		private Builder(){
			// UDP 如果60秒没有消息. 路由信息也会丢失. 不需要10分钟.
			KcpBootstrapParams.this.readIdleCheckSeconds = 60;
		}

		public Builder setDependOnTcpWs() {
			KcpBootstrapParams.this.dependOnTcpWs = true;
			return this;
		}

		@Override
		public Builder setPort(int port) {
			throw new CustomException("Not Support! Use setPorts()");
		}

		public Builder setPorts(Set<Integer> ports) {
			KcpBootstrapParams.this.ports = ImmutableSet.copyOf(ports);
			return this;
		}

		public Builder setKcpParam(int snd_wnd, int rcv_wnd, int interval, boolean noDelay, int mtu, int fastResend, boolean noCwnd) {
			KcpBootstrapParams.this.kcpParam = new KcpParam(snd_wnd, rcv_wnd, interval, noDelay, mtu, fastResend, noCwnd);
			return this;
		}

		@Override
		public KcpBootstrapParams build() {
			return this.newParams();
		}

		@Override
		protected KcpBootstrapParams newParams() {
			if (protocolHeaderType == null) {
				throw new NullPointerException("Must set IProtocolHeaderType for Listener!");
			}

			return KcpBootstrapParams.this;
		}
	}

	public static class KcpParam {
		public static final KcpParam DEFAULT_KCP_PARAM = new KcpParam(512, 512, 20, true, 512, 2, true);
		/**
		 * 发送窗口大小.
		 * 响应客户端的接收窗大小也要高于该值.
		 */
		private final int snd_wnd;
		/**
		 * 接收窗大小. 要大于客户端的发送窗大小
		 */
		private final int rcv_wnd;
		/**
		 * 间隔. 内部flush刷新间隔
		 */
		private final int interval;
		/**
		 * 无延迟
		 */
		private final boolean noDelay;
		/**
		 * 传输单元大小.
		 */
		private final int mtu;
		/**
		 * 跳过几次后, 快速重传
		 */
		private final int fastResend;
		/**
		 * 取消拥塞控制
		 */
		private final boolean noCwnd;

		public KcpParam(int snd_wnd, int rcv_wnd, int interval, boolean noDelay, int mtu, int fastResend, boolean noCwnd) {
			this.snd_wnd = snd_wnd;
			this.rcv_wnd = rcv_wnd;
			this.interval = interval;
			this.noDelay = noDelay;
			this.mtu = mtu;
			this.fastResend = fastResend;
			this.noCwnd = noCwnd;
		}

		public int getSnd_wnd() {
			return snd_wnd;
		}

		public int getRcv_wnd() {
			return rcv_wnd;
		}

		public int getInterval() {
			return interval;
		}

		public boolean isNoDelay() {
			return noDelay;
		}

		public int getMtu() {
			return mtu;
		}

		public int getFastResend() {
			return fastResend;
		}

		public boolean isNoCwnd() {
			return noCwnd;
		}
	}
}
