package org.qiunet.test.cross.pool;

import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qiunet.cross.actor.auth.CrossPlayerAuthRequest;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.cross.pool.NodeChannelPool;
import org.qiunet.cross.pool.NodeChannelTrigger;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.NodeClientSession;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.proto.req.PoolTestReq;
import org.qiunet.test.handler.proto.ProtocolId;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @author qiunet
 * 2023/11/18 22:25
 */
public class PoolClient {
	private static final Map<Long, NodeClientSession> sessionMap = Maps.newConcurrentMap();
	private static final CountDownLatch latch = new CountDownLatch(100);
	private static final int reqCount = (int) latch.getCount();
	private static final int thread = 20;

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ServerInfo serverInfo = ServerInfo.valueOf(Constants.LOGIC_SERVER_ID, Constants.LOGIC_SERVER_PORT, Constants.LOGIC_NODE_PORT);
		ClassScanner.getInstance(ScannerType.SERVER).addParam(ScannerParamKey.CUSTOM_SERVER_INFO, serverInfo).scanner();

		Bootstrap bootstrap = new Bootstrap();
		Class<? extends SocketChannel> socketChannelClz = Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
		bootstrap.remoteAddress("localhost", Constants.CROSS_NODE_PORT);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.group(ServerConstants.WORKER);
		bootstrap.channel(socketChannelClz);

		try (NodeChannelPool pool = new NodeChannelPool(bootstrap, new Trigger(), true, ServerConstants.MAX_SOCKET_MESSAGE_LENGTH, 5)){
			for (int i = 1; i <= thread; i++) {
				int finalI = i;
				new Thread(
					() -> {
						NodeClientSession nodeClientSession = new NodeClientSession(NodeSessionType.CROSS_PLAYER, pool, finalI);
						sessionMap.put((long)finalI, nodeClientSession);

						nodeClientSession.sendMessage(CrossPlayerAuthRequest.valueOf(finalI, 100, String.valueOf(finalI)), true);
						for (int i1 = 0; i1 < reqCount / thread; i1++) {
							nodeClientSession.sendMessage(PoolTestReq.valueOf(MathUtil.random(10000)), true);
						}
					}, "i:" + i).start();

				Thread.sleep(100);
			}

			latch.await();
			LoggerType.DUODUO_CROSS.info("TEST FINISHED");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class Trigger implements NodeChannelTrigger {
		@Override
		public void response0(ISession session, Channel channel, MessageContent data) {
			if (data.getProtocolId() != ProtocolId.Pool.TEST_RSP) {
				return;
			}
			latch.countDown();
		}

		@Override
		public ISession getNodeSession(Channel channel, INodeServerHeader header) {
			return sessionMap.get(header.id());
		}
	}
}
