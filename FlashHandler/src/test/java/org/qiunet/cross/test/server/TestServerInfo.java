package org.qiunet.cross.test.server;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.utils.json.JsonUtil;

/***
 *
 *
 * @author qiunet
 * 2021/10/28 16:41
 **/
public class TestServerInfo {
	private static final String host = "192.168.1.124";
	private static final int communicationPort = 8081;
	private static final int serverId = 1;
	private static final int serverPort = 8080;
	private static final int onlineNum = 11;

	@Test
	public void test(){
		ServerInfo serverInfo0 = ServerInfo.valueOf(serverId, host, serverPort, communicationPort);
		serverInfo0.put("onlineNum", onlineNum);
		this.assert0(serverInfo0);

		String json = serverInfo0.toString();
		ServerInfo serverInfo1 = JsonUtil.getGeneralObject(json, ServerInfo.class);
		this.assert0(serverInfo1);
	}

	private void assert0(ServerInfo serverInfo0) {
		Assert.assertEquals(host, serverInfo0.getHost());
		Assert.assertEquals(serverId, serverInfo0.getServerId());
		Assert.assertEquals(onlineNum, serverInfo0.get("onlineNum"));
		Assert.assertEquals(serverPort, serverInfo0.getServerPort());
		Assert.assertEquals(communicationPort, serverInfo0.getNodePort());
	}
}
