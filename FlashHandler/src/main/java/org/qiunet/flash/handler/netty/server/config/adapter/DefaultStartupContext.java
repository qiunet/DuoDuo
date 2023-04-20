package org.qiunet.flash.handler.netty.server.config.adapter;

import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerCloseRsp;

/***
 * 默认的
 *
 * @author qiunet
 * 2023/4/5 10:18
 */
public class DefaultStartupContext implements IStartupContext  {

	@Override
	public boolean userServerValidate(ISession session) {
		if (ServerNodeManager.isServerClosed()) {
			session.sendMessage(ServerCloseRsp.valueOf());
			return false;
		}
		return true;
	}

}
