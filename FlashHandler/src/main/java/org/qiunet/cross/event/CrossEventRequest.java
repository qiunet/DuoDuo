package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.IDataToString;
import org.qiunet.flash.handler.util.SkipProtoGenerator;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.IEventData;
import org.qiunet.utils.string.ToString;

/***
 *
 *
 * @author qiunet
 * 2020-10-15 17:00
 */
@SkipProtoGenerator
@ChannelData(ID = IProtocolId.System.CROSS_EVENT, desc = "跨服事件处理")
public class CrossEventRequest implements IChannelData, IDataToString {
	@Protobuf(description = "事件的className")
	private String className;
	@Protobuf(description = "事件反序列化的数据.")
	private String datas;
	@Ignore
	private IEventData data;

	public static CrossEventRequest valueOf(IEventData data) {
		Preconditions.checkNotNull(data);

		CrossEventRequest request = new CrossEventRequest();
		request.className = data.getClass().getName();
		request.datas = JsonUtil.toJsonString(data);
		request.data = data;
		return request;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDatas() {
		return datas;
	}

	public void setDatas(String datas) {
		this.datas = datas;
	}

	public IEventData getData() {
		if (data == null) {
			try {
				Class<?> aClass = Class.forName(className);
				data = (IEventData) JsonUtil.getGeneralObjWithField(datas, aClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	@Override
	public String _toString() {
		return "CrossEvent_"+ ToString.toString(data);
	}
}
