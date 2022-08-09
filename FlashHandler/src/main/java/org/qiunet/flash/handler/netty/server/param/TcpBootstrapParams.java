package org.qiunet.flash.handler.netty.server.param;

import com.google.common.collect.Lists;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.PollChooserFactory;
import org.qiunet.utils.system.OSUtil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {
	/**
	 * 选择器
	 */
	private PollChooserFactory.PollChooser<Integer> udpPortChooser;
	/**
	 * 放开udp
	 */
	private KcpBootstrapParams.KcpParam kcpParam;
	/**
	 *  udp 端口
	 */
	private int udpPortOffset;

	private int udpPortCount;

	private TcpBootstrapParams(){}

	/**
	 * 获取一个udp端口给客户端
	 * @return
	 */
	public int nextUdpPort() {
		return udpPortChooser.next();
	}
	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		TcpBootstrapParams params = new TcpBootstrapParams();
		return params.new Builder();
	}

	public boolean isUdpOpen() {
		return kcpParam != null;
	}

	/**
	 * 转为kcp 参数
	 * @return
	 */
	public KcpBootstrapParams toKcpBootstrapParams() {
		if (! isUdpOpen()) {
			throw new IllegalArgumentException("Can not use to kcp!");
		}

		if (this.udpPortCount == 0) {
			throw new IllegalArgumentException("kcp port count need great than 1!");
		}

		Set<Integer> udpPorts = IntStream.range(0, this.udpPortCount).map(i -> i + this.udpPortOffset + this.port).boxed().collect(Collectors.toSet());

		this.udpPortChooser = PollChooserFactory.DEFAULT.newChooser(Lists.newArrayList(udpPorts));
		return KcpBootstrapParams.custom()
				.setReadIdleCheckSeconds(this.readIdleCheckSeconds)
				.setProtocolHeaderType(this.protocolHeaderType)
				.setMaxReceivedLength(this.maxReceivedLength)
				.setStartupContext(this.startupContext)
				.setEncryption(this.encryption)
				.setServerName(this.serverName)
				.setPorts(udpPorts)
				.setDependOnTcpWs()
				.build();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<TcpBootstrapParams, Builder> {

		private Builder(){}

		@Override
		protected TcpBootstrapParams newParams() {
			if (protocolHeaderType == null) {
				throw new NullPointerException("Must set IProtocolHeaderType for Listener!");
			}

			return TcpBootstrapParams.this;
		}
		public Builder setUdpOpen() {
			return this.setUdpOpen(100);
		}

		public Builder setUdpOpen(int udpPortOffset) {
			return setUdpOpen(udpPortOffset, OSUtil.availableProcessors());
		}
		/**
		 * 设置 udp open
		 * @return
		 */
		public Builder setUdpOpen(int udpPortOffset, int udpPortCount) {
			TcpBootstrapParams.this.kcpParam = KcpBootstrapParams.KcpParam.DEFAULT_KCP_PARAM;
			return this.setUdpPorts(udpPortOffset, udpPortCount);
		}

		/**
		 * 设置udp 的端口
		 * @param udpPortOffset 与tcp端口偏移数. 比如tcp为8880 则udp为 8880 + udpPortOffset
		 * @param udpPortCount 有几个kcp端口.
		 * @return
		 */
		public Builder setUdpPorts(int udpPortOffset, int udpPortCount) {
			if (!OSUtil.isLinux() && udpPortCount > 1) {
				LoggerType.DUODUO_FLASH_HANDLER.warn("Udp port count reset to 1");
				udpPortCount = 1;
			}
			TcpBootstrapParams.this.udpPortOffset = udpPortOffset;
			TcpBootstrapParams.this.udpPortCount = udpPortCount;
			return this;
		}
		/**
		 * 设置 udp open
		 * @return
		 */
		public Builder setUdpOpen(int snd_wnd, int rcv_wnd, int interval, boolean noDelay, int mtu, int fastResend, boolean noCwnd) {
			TcpBootstrapParams.this.kcpParam = new KcpBootstrapParams.KcpParam(snd_wnd, rcv_wnd, interval, noDelay, mtu, fastResend, noCwnd);
			return this;
		}
	}
}
