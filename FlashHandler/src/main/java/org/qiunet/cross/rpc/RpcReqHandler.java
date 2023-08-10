package org.qiunet.cross.rpc;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.ClassUtil;
import org.qiunet.utils.thread.ThreadPoolManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/***
 * 处理rpc请求
 *
 * @author qiunet
 * 2023/5/5 17:30
 */
public class RpcReqHandler extends PersistConnPbHandler<ServerNode, RouteRpcReq> {

	@Override
	public void handler(ServerNode serverNode, IPersistConnRequest<RouteRpcReq> context) throws Exception {
		RouteRpcReq requestData = context.getRequestData();
		IRpcRequest rpcRequest = (IRpcRequest) requestData.getData().getData();
		Class<?> aClass = Class.forName(requestData.getClz());
		Method method =  aClass.getMethod(requestData.getMtd(), rpcRequest.getClass());

		if (IPlayer.class.isAssignableFrom(rpcRequest.getClass()) && ((IPlayer) rpcRequest).getId() > 0) {
			AbstractUserActor actor = UserOnlineManager.instance.returnActor(((IPlayer) rpcRequest).getId());
			actor.addMessage(h -> {
				this.sendMessage(serverNode, requestData, method);
			});
			return;
		}

		ThreadPoolManager.NORMAL.submit(() -> {
			try {
				this.sendMessage(serverNode, requestData, method);
			} catch (Exception e) {
				LoggerType.DUODUO_FLASH_HANDLER.error("Exception:" , e);
			}
		});
	}

	private void sendMessage(ServerNode serverNode, RouteRpcReq requestData, Method method) throws Exception {
		IRpcRequest rpcRequest = (IRpcRequest) requestData.getData().getData();

		Object instance = null;
		if (! Modifier.isStatic(method.getModifiers())) {
			instance = ClassUtil.getInstanceOfClass(method.getDeclaringClass());
		}

		Object ret = ReflectUtil.makeAccessible(method).invoke(instance, rpcRequest);
		RouteRpcRsp rsp = RouteRpcRsp.valueOf(requestData.getReqId(), ret);
		serverNode.sendMessage(rsp);
	}
}
