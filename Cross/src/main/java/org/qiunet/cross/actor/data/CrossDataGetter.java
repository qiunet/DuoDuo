package org.qiunet.cross.actor.data;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.transaction.TransactionFuture;
import org.qiunet.cross.transaction.TransactionManager;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.exceptions.CustomException;


/***
 *
 *
 * @author qiunet
 * 2020-10-28 11:35
 */
public final class CrossDataGetter<Data extends BaseCrossTransferData> {
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
		return crossData.getKey();
	}

	/**
	 * 获得对应的对象.
	 * @return
	 */
	public Data get(){
		return loader.get();
	}

	private Data get0(){
		CrossDataTransactionRequest request = CrossDataTransactionRequest.valueOf(getKey(), crossPlayerActor.getPlayerId());
		TransactionFuture<CrossDataTransactionResponse> transactionFuture = TransactionManager.instance.beginTransaction(crossPlayerActor.getServerId(), request);
		try {
			CrossDataTransactionResponse response = transactionFuture.get();
			return ProtobufDataManager.decode(crossData.getDataClass(), response.getBytes());
		} catch (Exception e) {
			throw new CustomException(e, "Exception");
		}
	}
}
