package org.qiunet.test.cross.common.rpc;

/***
 *
 * @author qiunet
 * 2020-10-23 20:56
 **/
public class TestRpcHandler {

	public static TestRpcRsp handler(TestRpcReq req) {
		return TestRpcRsp.valueOf(req.getPlayerId());
	}
}
