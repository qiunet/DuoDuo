package org.qiunet.cross.transaction;


import java.util.function.Function;

/***
 * 一个事务得处理.
 *
 * @author qiunet
 * 2020-10-22 17:38
 */
public interface ITransactionHandler<Req extends BaseTransactionRequest, Resp extends BaseTransactionResponse> {

	/**
	 * 处理事务
	 * 自己在线程安全的地方调用 {@link DTransaction#handler(Function)}
	 * transaction有2秒超时时间. 2秒没有处理. 就会超时.
	 *
	 * @param transaction 事务处理的对象.
	 */
	void handler(DTransaction<Req, Resp> transaction);
}
