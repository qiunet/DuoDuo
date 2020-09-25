package org.qiunet.flash.handler.context.request.data.pb;

import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
public interface IpbData {
	/**
	 * 转换为byte[]
	 * @return
	 */
	default <T extends IpbData> byte [] toByteArray(){
		return ProtobufDataManager.encode((Class<T>)this.getClass(), (T)this);
	}
}
