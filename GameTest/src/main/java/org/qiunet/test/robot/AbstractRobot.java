package org.qiunet.test.robot;

import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.test.response.ILongConnResponse;
import org.qiunet.test.response.annotation.support.ResponseMapping;
import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/12/6
 */
public abstract class AbstractRobot< Info extends IRobotInitInfo> implements IRobot<Info> {
	protected QLogger logger = LoggerManager.getLogger(LoggerType.GAME_TEST);
	private LongConnResponseTrigger trigger = new LongConnResponseTrigger();
	private AbstractRobot robot;
	/***
	 * 长连接的session map
	 */
	private Map<String, ILongConnClient> clients = new HashMap<>();

	private String brokeReason;

	protected int uid;
	/***游戏的token */
	protected String token;
	/***平台的信息*/
	protected Info info;

	private List<ITestCase> testCases;

	public AbstractRobot(List<ITestCase> testCases, Info info) {
		this.testCases = testCases;
		this.info = info;
		this.robot = this;
	}

	@Override
	public int getUid() {
		return uid;
	}

	@Override
	public Info getRobotInitInfo() {
		return info;
	}

	@Override
	public ILongConnClient getLongConnClient(IServer server) {
		if (clients.containsKey(server.getName())) return clients.get(server.getName());
		ILongConnClient connClient = null;
		switch (server.getType()) {
			case WEB_SOCKET:
				connClient = new NettyWebsocketClient(server.uri(), trigger);
				break;
			case TCP:
				connClient = new NettyTcpClient(new InetSocketAddress(server.uri().getHost(), server.uri().getPort()), trigger);
				break;
		}
		clients.put(server.getName(), connClient);
		return clients.get(server.getName());
	}

	@Override
	public void brokeRobot(String brokeReason) {
		this.brokeReason = brokeReason;
	}
	@Override
	public void run() {
		for (ITestCase testCase : testCases) {
			if (brokeReason != null) {
				logger.error("中断错误: "+ brokeReason);
				break;
			}

			boolean conditionJudgePass = testCase.conditionJudge(this);
			logger.info("OpenId ["+info.getOpenId()+"]("+uid+") ["+(conditionJudgePass?"Running":"Miss..........")+"] TestCase ["+testCase.getClass().getSimpleName()+"]");
			if (conditionJudgePass) {
				testCase.sendRequest(this);
			}else if (testCase.cancelIfConditionMiss()){
				logger.info("程序终止....");
				// 如果条件不满足, 就终止的case
				break;
			}

		}
	}
	private Thread currThread;
	private int parkResponseId;
	@Override
	public void parkForResponseID(int parkResponseId) {
		this.parkResponseId = parkResponseId;
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	private class LongConnResponseTrigger implements ILongConnResponseTrigger {

		@Override
		public void response(MessageContent data) {
			if (data.getProtocolId() == parkResponseId ) {
				LockSupport.unpark(currThread);
			}

			ILongConnResponse response = ResponseMapping.getInstance().getResponse(data.getProtocolId());
			response.response(robot, data);
		}
	}
}
