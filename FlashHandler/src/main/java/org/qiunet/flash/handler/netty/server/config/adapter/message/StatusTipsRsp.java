package org.qiunet.flash.handler.netty.server.config.adapter.message;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
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
@ChannelData(ID = IProtocolId.System.ERROR_STATUS_TIPS_RSP, desc = "错误信息提示响应")
public class StatusTipsRsp extends IChannelData {
	/**
	 * 策划配置最好. 客户端读表. 配合参数生成提示
	 */
	@Protobuf(description = "提示id, GameStatus 服务器定义给出.")
	private int status;
	@Protobuf(description = "占位符(占位符号客户端自己定义)参数")
	private List<String> params;
	@Protobuf(description = "大概错误描述.客户端控制台打印自己看")
	private String desc;
	@Ignore
	private static final Map<Integer, StatusTipsRsp> cached = Maps.newConcurrentMap();

	public StatusTipsRsp(){}

	private StatusTipsRsp(int status, String desc, Object... params) {
		if (params != null && params.length > 0) {
			this.params = Arrays.stream(params).map(String::valueOf).collect(Collectors.toList());
		}
		this.status = status;
		this.desc = desc;
	}

	public static StatusTipsRsp valueOf(IGameStatus status, Object... params) {
		if (params == null || params.length == 0) {
			return cached.computeIfAbsent(status.getStatus(), key -> new StatusTipsRsp(status.getStatus(), status.getDesc()));
		}
		return new StatusTipsRsp(status.getStatus(), status.getDesc(), params);
	}

	public static StatusTipsRsp valueOf(StatusResultException ex) {
		return valueOf(ex.getStatus(), ex.getArgs());
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public int getStatus() {
		return status;
	}

	public List<String> getParams() {
		return params;
	}
}
