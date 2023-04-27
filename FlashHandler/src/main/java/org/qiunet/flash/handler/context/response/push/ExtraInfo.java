package org.qiunet.flash.handler.context.response.push;

import java.util.HashMap;
import java.util.Map;

/***
 * 额外信息记录
 * @author qiunet
 * 2023/3/29 15:52
 */
abstract class ExtraInfo implements IExtraInfo {
	/**
	 * 额外的数据
	 */
	private Map<String, Object> data;
	/**
	 * 额外信息
	 */
	private int extraInfo;

	@Override
	public int getExtraInfo() {
		return extraInfo;
	}

	@Override
	public IChannelMessage<?> asCrossPlayerMsg() {
		this.extraInfo = ExtraInfoType.CROSS_PLAYER_MSG.setVal(this.extraInfo);
		return (IChannelMessage<?>)this;
	}

	@Override
	public IChannelMessage<?> asServerNodeMsg() {
		this.extraInfo = ExtraInfoType.SERVER_NODE_MSG.setVal(this.extraInfo);
		return (IChannelMessage<?>)this;
	}

	@Override
	public IChannelMessage<?> asKcpMsg() {
		this.extraInfo = ExtraInfoType.KCP.setVal(this.extraInfo);
		return (IChannelMessage<?>)this;
	}

	@Override
	public IChannelMessage<?> needFlush(boolean flush) {
		if (flush) {
			this.extraInfo = ExtraInfoType.FLUSH.setVal(this.extraInfo);
		}
		return (IChannelMessage<?>)this;
	}

	@Override
	public void recycle() {
		if (data != null) {
			data.clear();
		}
		this.extraInfo = 0;
	}

	@Override
	public synchronized IChannelMessage<?> add(String key, Object val) {
		if (data == null) {
			data = new HashMap<>(8);
		}
		data.put(key, val);
		return (IChannelMessage<?>)this;
	}

	@Override
	public Object get(String key) {
		if (data == null) {
			return null;
		}
		synchronized (this) {
			return data.get(key);
		}
	}
}
