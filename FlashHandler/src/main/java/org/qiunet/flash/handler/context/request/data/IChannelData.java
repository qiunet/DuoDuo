package org.qiunet.flash.handler.context.request.data;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import io.netty.buffer.ByteBuf;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.utils.string.ToString;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
@ProtobufClass
public abstract class IChannelData {
	/**
	 * 转换为ByteBuf 最终记得要release
	 * @return
	 */
	public ByteBuf toByteBuf() {
		return ProtobufDataManager.encodeToByteBuf(this);
	}
	/**
	 * 转换为byte[]
	 * @return
	 */
	public byte[] toByteArray(){
		return ProtobufDataManager.encodeToByteArray(this);
	}
	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	public DefaultProtobufMessage buildChannelMessage(){
		return DefaultProtobufMessage.valueOf(protocolId(), this);
	}
	/**
	 * 跳过交互输出, 输出内容为 {@link #_toString()}
	 * @return true 打印 false 跳过
	 */
	public boolean debugOut() {
		return ! getClass().isAnnotationPresent(SkipDebugOut.class)|| ServerConfig.isDebugEnv();
	}

	/**
	 * 打印该对象
	 * @return 对象有效字符串.
	 */
	public String _toString(){
		return ToString.toString(this);
	}
	/**
	 * channel data to bytebuf 后.
	 * 会调用该方法. 如果需要回收的东西.
	 * 可以写这个里面
	 */
	public void finishCycle() {}
	/**
	 * 得到protocolId
	 * @return
	 */
	public int protocolId() {
		return getClass().getAnnotation(ChannelData.class).ID();
	}
}
