package org.qiunet.test.cross.common.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.test.cross.common.proto.req.PoolTestReq;
import org.qiunet.test.cross.common.proto.resp.PoolTestRsp;

/**
 * @author qiunet
 * 2023/11/20 11:21
 */
public class PoolTestHandler extends PersistConnPbHandler<CrossPlayerActor, PoolTestReq> {

	@Override
	public void handler(CrossPlayerActor playerActor, IPersistConnRequest<PoolTestReq> context) throws Exception {
		playerActor.sendMessage(PoolTestRsp.valueOf(context.getRequestData().getId()));
	}
}
