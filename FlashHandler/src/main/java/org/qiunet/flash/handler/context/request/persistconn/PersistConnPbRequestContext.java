package org.qiunet.flash.handler.context.request.persistconn;

import com.google.common.base.Preconditions;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.protocol.CommonProtocolCD;
import org.qiunet.flash.handler.common.player.protocol.IgnoreCommonProtocolCDCheck;
import org.qiunet.flash.handler.context.request.check.RequestCheckList;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;
import org.qiunet.utils.pool.ObjectPool;

/**
 * 该对象会回收. 所以只能在本线程用. addMessage 后. 就会回收掉
 *
 * Created by qiunet.
 * 17/12/2
 */
public class PersistConnPbRequestContext<RequestData extends IChannelData, P extends IMessageActor<P>>
		extends AbstractPersistConnRequestContext<RequestData, P> {

	private static final ObjectPool<PersistConnPbRequestContext> RECYCLER = new ObjectPool<>() {
		@Override
		public PersistConnPbRequestContext newObject(Handle<PersistConnPbRequestContext> handler) {
			return new PersistConnPbRequestContext(handler);
		}
	};

	private final ObjectPool.Handle<PersistConnPbRequestContext> recyclerHandle;

	public PersistConnPbRequestContext(ObjectPool.Handle<PersistConnPbRequestContext> recyclerHandle) {
		this.recyclerHandle = recyclerHandle;
	}

	public static PersistConnPbRequestContext valueOf(ISession session, MessageContent content) {
		PersistConnPbRequestContext context = RECYCLER.get();
		context.init(session, content);
		return context;
	}

	public void init(ISession session, MessageContent content) {
		super.init(session, content);
	}

	private void recycle() {
		this.requestData = null;
		this.attributes = null;
		this.reqSequence = 0;
		this.session = null;
		this.handler = null;

		this.recyclerHandle.recycle();
	}

	@Override
	public void execute(P p) throws Exception {
		Preconditions.checkArgument(this.session != null);
		long startTime = System.currentTimeMillis();
		try {
			// 请求校验
			if (this.requestValid(startTime)) {
				// 请求处理
				this.handlerRequest();
			}
		} finally {
			// 太频繁的请求不记录数据
			if (getRequestData().debugOut()) {
				long useTime = System.currentTimeMillis() - startTime;
				this.getHandler().recordUseTime(useTime);
				if (useTime > 500) {
					logger.error("Request {} use [{}] ms to executor!", this, useTime);
				}
			}
			this.recycle();
		}
	}

	@Override
	public void handlerRequest() throws Exception{
		P messageActor = (P) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);

		if (messageActor instanceof CrossPlayerActor && getHandler() instanceof ITransmitHandler) {
			((ITransmitHandler) getHandler()).crossHandler(((CrossPlayerActor) messageActor), getRequestData());
		}else {
			FacadePersistConnRequest<RequestData, P> facadeWebSocketRequest = FacadePersistConnRequest.valueOf(this);
			try {
				((IPersistConnHandler) getHandler()).handler(messageActor, facadeWebSocketRequest);
			}finally {
				facadeWebSocketRequest.recycle();
			}
		}
	}


	/**
	 * 对请求进行检查.  会抛出{@link org.qiunet.flash.handler.context.status.StatusResultException}异常.
	 */
	private boolean requestValid(long now) {
		P messageActor = (P) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		RequestData channelData = getRequestData();

		if (requestData == null) {
			logger.error("RequestData is null for case playerId {} , protocol: {}", messageActor.getIdentity(), getHandler().getClass().getSimpleName());
			return false;
		}

		if (handler.needAuth() && ! messageActor.isAuth()) {
			logger.info("Handler [{}] need auth. but session {} not auth!", handler.getClass().getSimpleName(), messageActor.getSession());
			// 先不管. 客户端重连可能有问题. 不能掐掉
			//ChannelUtil.getSession(channel).close(CloseCause.ERR_REQUEST);
			return false;
		}

		if (logger.isInfoEnabled() && requestData.debugOut()) {
			logger.info("[{}] [{}({})] <<< {}", messageActor.getIdentity(), session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY), session.aliasId(), getRequestData()._toString());
		}

		RequestCheckList requestCheckList = ChannelDataMapping.getParamChecks(channelData.getClass());
		if (requestCheckList != null) {
			requestCheckList.check(session, channelData);
		}

		// 通用协议cd检查
		CommonProtocolCD commonProtocolCD = session.getAttachObj(ServerConstants.COMMON_PROTOCOL_CD_CHECK_KEY);
		if (commonProtocolCD == null) {
			return true;
		}

		boolean invalidRequest = commonProtocolCD.isInvalidRequest(channelData.protocolId(), now);
		if (invalidRequest && !channelData.getClass().isAnnotationPresent(IgnoreCommonProtocolCDCheck.class)) {
			throw StatusResultException.valueOf(IGameStatus.COMMON_PROTOCOL_CD_ING, channelData.protocolId());
		}

		return true;
	}
}
