package org.qiunet.flash.handler.netty.server.param.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * 错误信息提示响应
 *
 * @author qiunet
 * 2020-11-11 10:04
 */
@ProtobufClass(description = "错误信息提示响应")
@PbResponse(IProtocolId.System.ERROR_MESSAGE_TIPS_RESP)
public class MessageTipsResponse implements IpbResponseData {
	/**
	 * 策划配置最好. 客户端读表. 配合参数生成提示
	 */
	@Protobuf(description = "提示id, GameStatus 服务器定义给出.")
	private int status;
	@Protobuf(description = "占位符(占位符号客户端自己定义)参数")
	private List<String> params;

	private static final Map<Integer, MessageTipsResponse> cached = Maps.newConcurrentMap();

	public MessageTipsResponse(){}

	private MessageTipsResponse(int status, Object... params) {
		this.status = status;
		if (params != null && params.length > 0) {
			this.params = Arrays.stream(params).map(String::valueOf).collect(Collectors.toList());
		}
	}

	public static MessageTipsResponse valueOf(IGameStatus status, Object... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status.getStatus(), MessageTipsResponse::new);
		}
		return new MessageTipsResponse(status.getStatus(), params);
	}

	public static MessageTipsResponse valueOf(StatusResultException ex) {
		return valueOf(ex.getStatus(), ex.getArgs());
	}

	public int getStatus() {
		return status;
	}

	public List<String> getParams() {
		return params;
	}
}