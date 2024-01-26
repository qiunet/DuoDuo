package org.qiunet.cross.rdc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.common.CommMessageHandler;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.id.DefaultIdGenerator;
import org.qiunet.utils.id.IdGenerator;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.thread.ThreadPoolManager;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/***
 * 远程数据调用 管理
 *
 * @author qiunet
 * 2020-10-22 12:53
 */
public enum RdcManager {
	instance;
	private static final CommMessageHandler messageHandler = new CommMessageHandler();

	private static final Logger logger = LoggerType.DUODUO_CROSS.getLogger();

	private final IdGenerator<Long> idGenerator  = new DefaultIdGenerator();
	/**
	 * 保存映射关系
	 */
	private final Map<Long, DPromise> cacheRequests = Maps.newConcurrentMap();

	/**
	 * 异步远程数据调用
	 * 注意. consumer 线程不在一个线程了.
	 * 需要注意线程安全
	 * @param serverId
	 * @param req
	 * @param consumer
	 * @param <Req>
	 * @param <Resp>
	 */
	public <Req extends IRdcRequest, Resp extends IRdcResponse> void beginRdc(int serverId, Req req, BiConsumer<Resp, ? super Throwable> consumer) {
		RdcFuture<Resp> future = this.beginRdc(serverId, req);
		future.whenComplete(consumer);
	}
	/**
	 * 发起远程数据调用请求
	 * @param serverId 目标的serverId
	 * @param req 请求数据
	 * @param <Req>
	 * @param <Resp>
	 * @return
	 */

	public <Req extends IRdcRequest, Resp extends IRdcResponse> RdcFuture<Resp> beginRdc(int serverId, Req req) {
		return beginRdc(serverId, req, 3, TimeUnit.SECONDS);
	}

	/**
	 * 发起远程数据调用请求
	 * @param serverId 目标的serverId
	 * @param req 请求数据
	 * @param timeout 超时时间
	 * @param unit 超时时间单位
	 * @param <Req>
	 * @param <Resp>
	 * @return
	 */
	public <Req extends IRdcRequest, Resp extends IRdcResponse> RdcFuture<Resp> beginRdc(int serverId, Req req, int timeout, TimeUnit unit) {
		Preconditions.checkNotNull(req);
		DPromise<Resp> promise = new DCompletePromise<>();

		long reqId = idGenerator.makeId();
		this.cacheRequests.put(reqId, promise);
		RouteRdcReq routeRdcReq = RouteRdcReq.valueOf(reqId, req);
		RdcFuture<Resp> respRdcFuture = new RdcFuture<>(reqId, promise);
		if (serverId == ServerNodeManager.getCurrServerId()) {
			DRdc<Req, Resp> dRdc = new DRdc<>(reqId, req);
			this.handler(req, dRdc);
			return respRdcFuture;
		}

		ServerNodeManager.getNode(serverId, node -> {
			ChannelFuture channelFuture = node.sendMessage(routeRdcReq);
			channelFuture.addListener(f -> {
				if (f.isSuccess()) {
					respRdcFuture.beginCalTimeOut(timeout, unit);
				}else {
					promise.tryFailure(new CustomException(f.cause(), "Rdc [{}] send fail!", JsonUtil.toJsonString(req)));
				}
			});
		});
		return respRdcFuture;
	}
	/**
	 * 根据是否是玩家请求. 分发到各自的线程.
	 * @param req
	 * @param dRdc
	 */
	void handler(IRdcRequest req, DRdc dRdc) {
		if (req instanceof IPlayer) {
			messageHandler.runMessageWithMsgExecuteIndex((node) -> {
				AbstractUserActor actor = UserOnlineManager.instance.returnActor(((IPlayer) req).getId());
				actor.addMessage(a -> this.handler0(req, dRdc));
			}, String.valueOf(((IPlayer) req).getId()));
		}else {
			ThreadPoolManager.NORMAL.submit(() -> this.handler0(req, dRdc));
		}
	}

	/**
	 * 处理远程数据调用.如果异常. 正常情况抛出给外面.
	 * 否则打印
	 * @param req 请求数据
	 * @param dRdc rdc 对象
	 */
	private void handler0(IRdcRequest req, DRdc dRdc) {
		try {
			RdcManager0.handler(req.getClass(), dRdc);
		}catch (Throwable e) {
			DPromise dPromise = cacheRequests.get(dRdc.getReqId());
			if (dPromise != null) {
				dPromise.tryFailure(e);
			}else {
				logger.error("rdc error!", e);
			}
		}
	}
	/**
	 * 超时移除映射. 但是不保证对面是否真超时.
	 * 可能网络延迟了点. 实际处理完成了, 这个业务自己根据需求判断.
	 * @param id
	 */
	void removeRdc(long id){
		this.cacheRequests.remove(id);
	}

	/**
	 * 处理回来的数据
	 * @param response
	 */
	void completeRdc(RouteRdcRsp response) {
		Object obj = response.getData();
		Preconditions.checkNotNull(obj);

		DPromise dPromise = cacheRequests.get(response.getId());
		if (dPromise == null) {
			logger.error("Cross IRdcHandler id[{}] Class [{}] Data [{}] is invalid!", response.getId(), response.getClassName(), JsonUtil.toJsonString(obj));
			return;
		}

		dPromise.trySuccess(obj);
	}
}
