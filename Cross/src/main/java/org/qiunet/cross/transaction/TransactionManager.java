package org.qiunet.cross.transaction;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.node.ServerNode;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.id.DefaultIdGenerator;
import org.qiunet.utils.id.IdGenerator;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 12:53
 */
public enum TransactionManager {
	instance;
	private static final Logger logger = LoggerType.DUODUO_CROSS.getLogger();

	private final IdGenerator idGenerator  = new DefaultIdGenerator();
	/**
	 * 保存映射关系
	 */
	private final Map<Long, DPromise> cacheRequests = Maps.newConcurrentMap();
	/**
	 * 发起事务请求
	 * @param serverId 目标的serverId
	 * @param req 请求数据
	 * @param <Req>
	 * @param <Resp>
	 * @return
	 */
	public <Req extends BaseTransactionRequest, Resp extends BaseTransactionResponse> TransactionFuture<Resp> beginTransaction(int serverId, Req req) {
		return beginTransaction(serverId, req, 3, TimeUnit.SECONDS);
	}

	/**
	 * 发起事务请求
	 * @param serverId 目标的serverId
	 * @param req 请求数据
	 * @param timeout 超时时间
	 * @param unit 超时时间单位
	 * @param <Req>
	 * @param <Resp>
	 * @return
	 */
	public <Req extends BaseTransactionRequest, Resp extends BaseTransactionResponse> TransactionFuture<Resp> beginTransaction(int serverId, Req req, int timeout, TimeUnit unit) {
		Preconditions.checkNotNull(req);
		DPromise<Resp> promise = new DCompletePromise<>();

		long reqId = idGenerator.makeId();
		this.cacheRequests.put(reqId, promise);
		RouteTransactionRequest routeTransactionRequest = RouteTransactionRequest.valueOf(reqId, req);
		TransactionFuture<Resp> respTransactionFuture = new TransactionFuture<>(reqId, promise);
		if (serverId == ServerNodeManager.getCurrServerId()) {
			DTransaction<Req, Resp> dTransaction = new DTransaction<>(reqId, req);
			TransactionManager0.handler(req.getClass(), dTransaction);
			return respTransactionFuture;
		}

		ServerNode node = ServerNodeManager.getNode(serverId);
		IDSessionFuture channelFuture = node.sendMessage(routeTransactionRequest);
		channelFuture.addListener(f -> {
			if (f.isSuccess()) {
				respTransactionFuture.beginCalTimeOut(timeout, unit);
			}else {
				promise.tryFailure(new CustomException(f.cause(), "Transaction [{}] send fail!", JsonUtil.toJsonString(req)));
			}
		});
		return respTransactionFuture;
	}

	/**
	 * 超时移除映射. 但是不保证对面是否真超时.
	 * 可能网络延迟了点. 实际处理完成了, 这个业务自己根据需求判断.
	 * @param id
	 */
	void removeTransaction(long id){
		this.cacheRequests.remove(id);
	}

	/**
	 * 处理事务回来的数据
	 * @param response
	 */
	void completeTransaction(RouteTransactionResponse response) {
		Object obj = response.getData();
		Preconditions.checkNotNull(obj);

		DPromise dPromise = cacheRequests.get(response.getId());
		if (dPromise == null) {
			logger.error("Cross ITransactionHandler id[{}] Class [{}] Data [{}] is invalid!", response.getId(), response.getRespClassName(), JsonUtil.toJsonString(obj));
			return;
		}

		dPromise.trySuccess(obj);
	}
}
