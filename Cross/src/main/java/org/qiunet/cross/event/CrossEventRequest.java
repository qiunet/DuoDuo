package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 *
 * @author qiunet
 * 2020-10-15 17:00
 */
@ProtobufClass(description = "跨服的事件")
public class CrossEventRequest implements IpbRequestData {
	@Protobuf(description = "事件的className")
	private String className;
	@Protobuf(description = "事件反序列化的数据.")
	private byte[] datas;

	public static CrossEventRequest valueOf(String className, byte[] datas) {
		CrossEventRequest request = new CrossEventRequest();
		request.className = className;
		request.datas = datas;
		return request;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public byte[] getDatas() {
		return datas;
	}

	public void setDatas(byte[] datas) {
		this.datas = datas;
	}
}
