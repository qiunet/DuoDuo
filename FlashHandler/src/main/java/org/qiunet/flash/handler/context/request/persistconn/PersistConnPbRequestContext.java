package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.config.adapter.message.StatusTipsRsp;
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

	public static PersistConnPbRequestContext valueOf(ISession session, MessageContent content, Channel channel) {
		PersistConnPbRequestContext context = RECYCLER.get();
		context.init(session, content, channel);
		return context;
	}

	public void init(ISession session, MessageContent content, Channel channel) {
		super.init(session, content, channel);
	}

	private void recycle() {
		this.requestData = null;
		this.attributes = null;
		this.reqSequence = 0;
		this.session = null;
		this.handler = null;
		this.channel = null;

		this.recyclerHandle.recycle();
	}

	@Override
	public void execute(P p) throws Exception {
		try {
			this.handlerRequest();
		} catch (StatusResultException e) {
			this.sendMessage(StatusTipsRsp.valueOf(e), true);
		}finally {
			this.recycle();
		}
	}

	@Override
	public void handlerRequest() throws Exception{
		P messageActor = (P) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);

		if (getRequestData() == null) {
			logger.error("RequestData is null for case playerId {} , protocol: {}", messageActor.getIdentity(), getHandler().getClass().getSimpleName());
			return;
		}
		ChannelDataMapping.requestCheck(channel, getRequestData());

		if (handler.needAuth() && ! messageActor.isAuth()) {
			logger.error("Handler [{}] need auth. but session {} not authorize access!", handler.getClass().getSimpleName(), messageActor.getSession());
			// 先不管. 客户端重连可能有问题. 不能掐掉
			//ChannelUtil.getSession(channel).close(CloseCause.ERR_REQUEST);
			return;
		}
		long startTime = System.currentTimeMillis();
		if (logger.isInfoEnabled() && getRequestData().debugOut()) {
			logger.info("[{}] [{}({})] <<< {}", messageActor.getIdentity(), session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channel.id().asShortText(), getRequestData()._toString());
		}

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
		long useTime = System.currentTimeMillis() - startTime;
		this.getHandler().recordUseTime(useTime);
	}
}
