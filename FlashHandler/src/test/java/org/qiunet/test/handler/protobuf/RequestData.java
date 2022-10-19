package org.qiunet.test.handler.protobuf;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.util.List;

/***
 *
 * @author qiunet
 * 2022/10/19 10:57
 */
@ProtobufClass
public class RequestData implements IChannelData {

	@Protobuf(description = "xx")
	private String name;

	@Protobuf
	private int level;

	@Protobuf
	private List<Long> list;


	public RequestData() {}

	public static RequestData valueOf(String name, int level, List<Long> list){
		RequestData data = new RequestData();
	    data.level = level;
		data.name = name;
		data.list = list;
		return data;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<Long> getList() {
		return list;
	}

	public void setList(List<Long> list) {
		this.list = list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
