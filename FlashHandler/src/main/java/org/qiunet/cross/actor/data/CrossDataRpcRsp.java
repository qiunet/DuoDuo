package org.qiunet.cross.actor.data;


import org.qiunet.cross.rpc.TransferJsonData;

/***
 * 跨服获取玩家数据的事务请求数据
 *
 * @author qiunet
 * 2020-10-28 12:03
 */
public class CrossDataRpcRsp {

	private TransferJsonData jsonData;

	public static CrossDataRpcRsp valueOf(IUserTransferData data) {
		CrossDataRpcRsp request = new CrossDataRpcRsp();
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
}
