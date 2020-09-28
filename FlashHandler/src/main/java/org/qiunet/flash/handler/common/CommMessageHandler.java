package org.qiunet.flash.handler.common;

/***
 *
 *
 * @author qiunet
 * 2020-09-28 15:57
 */
public class CommMessageHandler extends MessageHandler<CommMessageHandler> {
	@Override
	protected String getIdent() {
		return "CommMessageHandler_"+hashCode();
	}
}
