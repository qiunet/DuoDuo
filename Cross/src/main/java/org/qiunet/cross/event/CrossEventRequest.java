package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.IDataToString;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.IEventData;

/***
 *
 *
 * @author qiunet
 * 2020-10-15 17:00
 */
@SkipProtoGenerator
@PbChannelData(ID = IProtocolId.System.CROSS_EVENT, desc = "跨服事件处理")
public class CrossEventRequest implements IpbChannelData, IDataToString {
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

	public IEventData getData() {
		if (data == null) {
			try {
				Class<? extends IEventData>aClass = (Class<? extends IEventData>) Class.forName(className);
				data = ProtobufDataManager.decode(aClass, datas);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	@Override
	public String _toString() {
		return "CrossEvent: ["+getData().getClass().getSimpleName()+": " + JsonUtil.toJsonString(getData())+"]";
	}
}
