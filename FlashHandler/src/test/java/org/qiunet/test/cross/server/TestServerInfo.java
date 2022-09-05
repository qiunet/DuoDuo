package org.qiunet.test.cross.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.net.NetUtil;

/***
 *
 *
 * @author qiunet
 * 2021/10/28 16:41
 **/
public class TestServerInfo {
	private static final int communicationPort = 8081;
	private static final int serverId = 1;
	private static final int serverPort = 8080;
	private static final int onlineNum = 11;

	@Test
	public void test(){
		ServerInfo serverInfo0 = ServerInfo.valueOf(serverId, serverPort, communicationPort);
		serverInfo0.put("onlineNum", onlineNum);
		this.assert0(serverInfo0);

		String json = serverInfo0.toString();
		ServerInfo serverInfo1 = JsonUtil.getGeneralObj(json, ServerInfo.class);
		this.assert0(serverInfo1);
	}

	private void assert0(ServerInfo serverInfo0) {
		Assertions.assertEquals(NetUtil.getInnerIp(), serverInfo0.getHost());
		Assertions.assertEquals(serverId, serverInfo0.getServerId());
		Assertions.assertEquals(onlineNum, serverInfo0.get("onlineNum"));
		Assertions.assertEquals(serverPort, serverInfo0.getServerPort());
		Assertions.assertEquals(communicationPort, serverInfo0.getNodePort());
	}
}
