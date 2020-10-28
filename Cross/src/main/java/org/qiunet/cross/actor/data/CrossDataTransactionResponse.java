package org.qiunet.cross.actor.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.cross.transaction.BaseTransactionResponse;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * 跨服获取玩家数据的事务请求数据
 *
 * @author qiunet
 * 2020-10-28 12:03
 */
public class CrossDataTransactionResponse extends BaseTransactionResponse {
	@Ignore
	private BaseTransferData data;

	@JSONField(serialize = false)
	private byte [] bytes;

	public static CrossDataTransactionResponse valueOf(BaseTransferData data) {
		CrossDataTransactionResponse request = new CrossDataTransactionResponse();
		request.data = data;
		request.bytes = ProtobufDataManager.encode(data);
		return request;
	}

	public BaseTransferData getData() {
		return data;
	}

	public void setData(BaseTransferData data) {
		this.data = data;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
