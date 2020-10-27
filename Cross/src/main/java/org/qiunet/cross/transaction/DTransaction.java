package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.timer.timeout.TimeOutFuture;
import org.qiunet.utils.timer.timeout.TimeOutManager;

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
		;
	}
	private long reqId;
	private REQ reqData;
	private ServerNode serverNode;
	private TimeOutFuture timeOutFuture;
	private AtomicReference<Status> status = new AtomicReference<>(Status.INIT);

	DTransaction(long reqId, REQ reqData, ServerNode serverNode) {
		this.reqId = reqId;
		this.reqData = reqData;
		this.serverNode = serverNode;

		this.timeOutFuture = TimeOutManager.newTimeOut(f -> this.compareAndSet(Status.INIT, Status.TIMEOUT), 2);
	}

	public void handler(Function<REQ, RESP> dataHandler) {
		if (! compareAndSet(Status.INIT, Status.OVER)) {
			throw new CustomException("ITransactionHandler is over! current status is [{}]", getStatus());
		}
		this.timeOutFuture.cancel();

		RESP response = dataHandler.apply(reqData);
		RouteTransactionResponse transactionResponse = RouteTransactionResponse.valueOf(reqId, response);
		serverNode.writeMessage(transactionResponse);
	}

	private boolean compareAndSet(Status expect, Status update) {
		return status.compareAndSet(expect, update);
	}

	public Status getStatus() {
		return status.get();
	}
}
