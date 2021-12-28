package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.timer.timeout.TimeOutFuture;
import org.qiunet.utils.timer.timeout.Timeout;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/***
 * 事务处理的类.
 *
 * @author qiunet
 * 2020-09-24 09:29
 */
public final class DTransaction<REQ extends BaseTransactionRequest, RESP extends BaseTransactionResponse> {
	public enum Status {
		/**初始化状态*/
		INIT,
		/**超时*/
		TIMEOUT,
		/**处理完毕*/
		OVER,
	}
	private final long reqId;
	private final REQ reqData;
	private final ServerNode serverNode;
	private final TimeOutFuture timeOutFuture;
	private final AtomicReference<Status> status = new AtomicReference<>(Status.INIT);

	DTransaction(long reqId, REQ reqData) {
		this(reqId, reqData, null);
	}
	DTransaction(long reqId, REQ reqData, ServerNode serverNode) {
		this.reqId = reqId;
		this.reqData = reqData;
		this.serverNode = serverNode;

		this.timeOutFuture = Timeout.newTimeOut(f -> this.compareAndSet(Status.INIT, Status.TIMEOUT), 2);
	}

	public void handler(Function<REQ, RESP> dataHandler) {
		if (! compareAndSet(Status.INIT, Status.OVER)) {
			throw new CustomException("ITransactionHandler is over! current status is [{}]", getStatus());
		}
		this.timeOutFuture.cancel();

		RESP response = dataHandler.apply(reqData);
		RouteTransactionRsp transactionResponse = RouteTransactionRsp.valueOf(reqId, response);
		if (serverNode != null) {
			serverNode.sendMessage(transactionResponse);
		}else {
			TransactionManager.instance.completeTransaction(transactionResponse);
		}
	}

	public REQ getReqData() {
		return reqData;
	}

	private boolean compareAndSet(Status expect, Status update) {
		return status.compareAndSet(expect, update);
	}

	public Status getStatus() {
		return status.get();
	}
}
