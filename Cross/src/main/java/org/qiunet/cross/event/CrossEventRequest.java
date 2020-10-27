package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;
import org.qiunet.listener.event.IEventData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 *
 *
 * @author qiunet
 * 2020-10-15 17:00
 */
@SkipProtoGenerator
@ProtobufClass(description = "跨服的事件")
public class CrossEventRequest implements IpbRequestData {
	@Protobuf(description = "事件的className")
	private String className;
	@Protobuf(description = "事件反序列化的数据.")
	private byte[] datas;
	@Ignore
	private IEventData data;

	public static CrossEventRequest valueOf(IEventData data) {
		Preconditions.checkNotNull(data);

		CrossEventRequest request = new CrossEventRequest();
		request.className = data.getClass().getName();
		request.datas = ProtobufDataManager.encode(data);
		request.data = data;
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

	@Override
	public String _toString() {
		return "CrossEvent: ["+data.getClass().getSimpleName()+": " + JsonUtil.toJsonString(data)+"]";
	}
}
