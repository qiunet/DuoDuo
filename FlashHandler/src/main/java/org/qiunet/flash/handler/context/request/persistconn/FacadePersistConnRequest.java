package org.qiunet.flash.handler.context.request.persistconn;


import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;

/**
 * Created by qiunet.
 * 17/12/2
 */
class FacadePersistConnRequest<RequestData, P extends IMessageActor<P>> implements IPersistConnRequest<RequestData> {
	private static final ThreadLocal<FacadePersistConnRequest> pool = ThreadLocal.withInitial(FacadePersistConnRequest::new);
	private AbstractPersistConnRequestContext<RequestData, P> context;

	private FacadePersistConnRequest() {}

	static <RequestData, P extends IMessageActor<P>> FacadePersistConnRequest<RequestData, P> valueOf(AbstractPersistConnRequestContext<RequestData, P> context) {
		FacadePersistConnRequest request = pool.get();
		request.context = context;
		return request;
	}

	void recycle(){
		this.context = null;
	}
	@Override
	public RequestData getRequestData() {
		return context.getRequestData();
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
	}

	@Override
	public Object getAttribute(String key) {
		return context.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		context.setAttribute(key, val);
	}

	@Override
	public ISession getSession() {
		return context.getSession();
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		return context.sendMessage(message, flush);
	}
}
