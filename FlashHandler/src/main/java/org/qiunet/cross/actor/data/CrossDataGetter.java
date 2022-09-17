package org.qiunet.cross.actor.data;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.transaction.TransactionFuture;
import org.qiunet.cross.transaction.TransactionManager;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.exceptions.CustomException;


/***
 *
 *
 * @author qiunet
 * 2020-10-28 11:35
 */
public final class CrossDataGetter<Data extends IUserTransferData> {
	private final LazyLoader<Data> loader;

	private final CrossData<Data> crossData;

	private final CrossPlayerActor crossPlayerActor;

	public CrossDataGetter(CrossPlayerActor crossPlayerActor, CrossData<Data> crossData) {
		this.crossData = crossData;
		this.crossPlayerActor = crossPlayerActor;
		this.loader = new LazyLoader<>(this::get0);
	}

	/**
	 * 获得 CrossData 的key
	 * @return
	 */
	public String getKey(){
		return crossData.name();
	}

	/**
	 * 是否有数据
	 * @return
	 */
	public boolean isPresent() {
		return loader.isNotNull();
	}
	/**
	 * 获得对应的对象.
	 * @return
	 */
	public Data get(){
		return loader.get();
	}

	private Data get0(){
		CrossDataTransactionReq request = CrossDataTransactionReq.valueOf(getKey(), crossPlayerActor.getPlayerId());
		TransactionFuture<CrossDataTransactionRsp> transactionFuture = TransactionManager.instance.beginTransaction(crossPlayerActor.getServerId(), request);
		try {
			CrossDataTransactionRsp response = transactionFuture.get();
			return (Data) response.getData();
		} catch (Exception e) {
			throw new CustomException(e, "CrossDataGetter Exception!!");
		}
	}
}
