package org.qiunet.cross.rdc;

import io.netty.util.Timeout;
import org.qiunet.cross.node.ServerNode;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.timer.timeout.TimeoutUtil;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/***
 * 远程数据调用处理的类.
 *
 * @author qiunet
 * 2020-09-24 09:29
 */
public final class DRdc<REQ extends IRdcRequest, RSP extends IRdcResponse> {
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
	private final Timeout timeOutFuture;
	private final AtomicReference<Status> status = new AtomicReference<>(Status.INIT);

	DRdc(long reqId, REQ reqData) {
		this(reqId, reqData, null);
	}
	DRdc(long reqId, REQ reqData, ServerNode serverNode) {
		this.reqId = reqId;
		this.reqData = reqData;
		this.serverNode = serverNode;

		this.timeOutFuture = TimeoutUtil.newTimeOut(f -> this.compareAndSet(Status.INIT, Status.TIMEOUT), 2);
	}

	public void handler(Function<REQ, RSP> dataHandler) {
		if (! compareAndSet(Status.INIT, Status.OVER)) {
			throw new CustomException("IRdcHandler is over! current status is [{}]", getStatus());
		}
		this.timeOutFuture.cancel();

		RSP response = dataHandler.apply(reqData);
		RouteRdcRsp rdcResponse = RouteRdcRsp.valueOf(reqId, response);
		if (serverNode != null) {
			serverNode.sendMessage(rdcResponse);
		}else {
			RdcManager.instance.completeRdc(rdcResponse);
		}
	}

	public long getReqId() {
		return reqId;
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
