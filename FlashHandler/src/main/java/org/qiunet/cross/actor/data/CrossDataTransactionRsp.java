package org.qiunet.cross.actor.data;

import org.qiunet.cross.transaction.ITransactionRsp;
import org.qiunet.cross.transaction.TransferJsonData;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

/***
 * 跨服获取玩家数据的事务请求数据
 *
 * @author qiunet
 * 2020-10-28 12:03
 */
public class CrossDataTransactionRsp implements ITransactionRsp, IDataToString {

	private TransferJsonData jsonData;

	public static CrossDataTransactionRsp valueOf(IUserTransferData data) {
		CrossDataTransactionRsp request = new CrossDataTransactionRsp();
		request.jsonData = new TransferJsonData(data);
		return request;
	}

	public TransferJsonData getJsonData() {
		return jsonData;
	}

	public void setJsonData(TransferJsonData jsonData) {
		this.jsonData = jsonData;
	}

	public IUserTransferData getData() {
		return (IUserTransferData) jsonData.getData();
	}

	@Override
	public String _toString() {
		return ToString.toString(getData());
	}
}
