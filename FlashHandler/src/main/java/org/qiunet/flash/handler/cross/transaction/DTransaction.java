package org.qiunet.flash.handler.cross.transaction;

import java.util.function.Function;

/***
 * 事务处理的类.
 *
 * @author qiunet
 * 2020-09-24 09:29
 */
public final class DTransaction<REQ extends ITransactionRequest, RESP extends ITransactionResponse> {


	public void handler(Function<REQ, RESP> dataHandler) {

	}
}
