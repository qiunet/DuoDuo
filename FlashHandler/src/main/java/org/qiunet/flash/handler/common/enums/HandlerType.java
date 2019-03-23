package org.qiunet.flash.handler.common.enums;

import org.qiunet.flash.handler.acceptor.ProcessAcceptor;
import org.qiunet.flash.handler.context.request.IRequestContext;

/**
 * handler的类型. 区分使用
 * @author qiunet
 *         Created on 17/3/7 17:22.
 */
public enum HandlerType {
	/**
	 * 包括http  https
	 */
	HTTP{
		@Override
		public void processRequest(IRequestContext context) {
			context.handler();
		}
	},
	/**
	 * tcp
	 *  但是udp可以使用该类型的context和handler
	 */
	TCP{
		@Override
		public void processRequest(IRequestContext context) {
			ProcessAcceptor.getInstance().process(context);
		}
	},
	/**
	 * webSocket
	 */
	WEB_SOCKET{
		@Override
		public void processRequest(IRequestContext context) {
			ProcessAcceptor.getInstance().process(context);
		}
	},
	/***
	 * 可靠的 UDP
	 */
	UDP {
		@Override
		public void processRequest(IRequestContext context) {
			ProcessAcceptor.getInstance().process(context);
		}
	},
	;

	public abstract void processRequest(IRequestContext context);
}
