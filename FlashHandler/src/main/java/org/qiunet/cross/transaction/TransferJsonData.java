package org.qiunet.cross.transaction;

import com.alibaba.fastjson.annotation.JSONField;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

/***
 * 传输一个对象
 *
 * @author qiunet
 * 2021/12/29 10:36
 */
@ProtobufClass
public class TransferJsonData implements IDataToString {
	@Ignore
	@JSONField(deserialize = false, serialize = false)
	private Object data;
	@Protobuf
	private String clazz;
	@Protobuf
	private String jsonData;

	public TransferJsonData() {}

	public TransferJsonData(Object data) {
		this.jsonData = JsonUtil.toJsonString(data);
		this.clazz = data.getClass().getName();
		this.data = data;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public Object getData() {
		if (data == null) {
			try {
				Class<?> aClass = Class.forName(clazz);
				data = JsonUtil.getGeneralObj(jsonData, aClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	@Override
	public String _toString() {
		return ToString.toString(getData());
	}
}
