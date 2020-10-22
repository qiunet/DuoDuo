package org.qiunet.cross.transaction;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.node.ServerNode;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.id.DefaultIdGenerator;
import org.qiunet.utils.id.IdGenerator;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.protobuf.ProtobufDataManager;
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

	private IdGenerator idGenerator  = new DefaultIdGenerator();
	/**
	 * 保存映射关系
	 */
	private Map<Long, DPromise> cacheRequests = Maps.newConcurrentMap();
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
		ServerNode node = ServerNodeManager.getNode(serverId);
		byte[] bytes = ProtobufDataManager.encode((Class<Req>)req.getClass(), req);
		RouteTransactionRequest routeTransactionRequest = RouteTransactionRequest.valueOf(reqId, req.getClass().getName(), bytes);
		node.writeMessage(routeTransactionRequest);

		return new TransactionFuture<>(reqId, promise, timeout, unit);
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
		Class<?> aClass;
		try {
			 aClass = Class.forName(response.getRespClassName());
		} catch (ClassNotFoundException e) {
			logger.error("Class {} is not exist!", response.getRespClassName());
			return;
		}

		Object obj = ProtobufDataManager.decode(aClass, response.getRespData());
		Preconditions.checkNotNull(obj);

		DPromise dPromise = cacheRequests.get(response.getId());
		if (dPromise == null) {
			logger.error("Cross ITransactionHandler id[{}] Class [{}] Data [{}] is invalid!", response.getId(), response.getRespClassName(), JsonUtil.toJsonString(obj));
			return;
		}

		dPromise.trySuccess(obj);
	}
}
