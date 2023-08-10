package org.qiunet.cross.rpc;

import com.google.common.collect.Maps;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/***
 *
 * @author qiunet
 * 2023/5/5 14:10
 */
public class RpcManager {
	private static final Map<Integer, RpcFuture> cached = Maps.newConcurrentMap();
	private static final AtomicInteger counter = new AtomicInteger();
	/**
	 * 跨服到某个serverId上, 并得到返回值.
	 * @param serverId 去哪个服务器上执行
	 * @param req 事件的主体
	 * @param consumer 返回值消费
	 * @param <E> 事件类型
	 * @param <R> 返回值类型
	 */
	public static  <E extends IRpcRequest, R> void rpcCall(
		int serverId, IRpcFunction<E, R> req, E reqData, BiConsumer<R, Throwable> consumer
	) {
		RpcFuture<R> rpcFuture = rpcCall(serverId, req, reqData);
		rpcFuture.whenComplete(consumer);
	}
	/**
	 * 跨服到某个serverId上, 并得到返回值.
	 * @param serverId 去哪个服务器上执行
	 * @param req 事件的主体
	 * @param <E> 事件类型
	 * @param <R> 返回值类型
	 * @return  RpcFuture 自己在外面异步 或者同步取数据
	 */
	public static <E extends IRpcRequest, R> RpcFuture<R> rpcCall(int serverId, IRpcFunction<E, R> req, E reqData) {
		RpcFuture<R> rpcFuture = new RpcFuture<>(counter.incrementAndGet(), new DCompletePromise<>());
		if (serverId == ServerNodeManager.getCurrServerId()) {
			if (IPlayer.class.isAssignableFrom(reqData.getClass()) && ((IPlayer) reqData).getId() > 0) {
				AbstractUserActor actor = UserOnlineManager.instance.returnActor(((IPlayer) reqData).getId());
				actor.addMessage(a -> {
					rpcFuture.getFuture().trySuccess(req.apply(reqData));
				});
			}else {
				try {
						rpcFuture.getFuture().trySuccess(req.apply(reqData));
				}catch (Exception e) {
					LoggerType.DUODUO_FLASH_HANDLER.error("Handler Rpc exception: " , e);
					rpcFuture.getFuture().tryFailure(e);
				}
			}
			return rpcFuture;
		}

		// 从function取出序列化方法

		try {
			Method writeReplaceMethod = req.getClass().getDeclaredMethod("writeReplace");
			SerializedLambda lambdaData = (SerializedLambda) ReflectUtil.makeAccessible(writeReplaceMethod).invoke(req);
			if (lambdaData.getImplMethodName().startsWith("lambda")) {
				throw new RuntimeException("Lambda expression need specify exist method!");
			}
			String declaringClass = lambdaData.getImplClass().replace("/", ".");
			RouteRpcReq routeRpcReq = RouteRpcReq.valueOf(rpcFuture.getId(), declaringClass, lambdaData.getImplMethodName(), reqData);
			rpcFuture.method = Class.forName(declaringClass).getDeclaredMethod(lambdaData.getImplMethodName(), reqData.getClass());
			ServerNodeManager.getNode(serverId, node -> {
				cached.put(rpcFuture.getId(), rpcFuture);
				node.sendMessage(routeRpcReq);
				rpcFuture.beginCalTimeOut(6);
			});
		} catch (Exception e) {
			LoggerType.DUODUO_FLASH_HANDLER.error("rpc call: ", e);
			rpcFuture.getFuture().tryFailure(e);
		}
		return rpcFuture;
	}

	/**
	 * 完成整个rpc
	 * @param reqId 请求id
	 * @param data 数据
	 */
	static void complete(int reqId, TransferJsonData data) {
		RpcFuture future = cached.remove(reqId);
		if (future == null || future.isDone()) {
			return;
		}
		Object generalObj = JsonUtil.getGeneralObj(data.getJsonData(), future.method.getGenericReturnType());
		future.getFuture().trySuccess(generalObj);
	}
	/**
	 * 移除映射关系
	 * @param id id
	 */
	static void removeMapping(int id) {
		cached.remove(id);
	}
}
