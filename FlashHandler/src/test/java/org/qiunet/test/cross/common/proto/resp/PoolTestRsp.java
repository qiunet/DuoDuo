package org.qiunet.test.cross.common.proto.resp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.test.handler.proto.ProtocolId;

/**
 * @author qiunet
 * 2023/11/20 11:21
 */
@ChannelData(ID = ProtocolId.Pool.TEST_RSP, desc = "池响应")
public class PoolTestRsp extends IChannelData {

	@Protobuf
	private int id;

	public static PoolTestRsp valueOf(int id){
		PoolTestRsp data = new PoolTestRsp();
	    data.id = id;
		return data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
