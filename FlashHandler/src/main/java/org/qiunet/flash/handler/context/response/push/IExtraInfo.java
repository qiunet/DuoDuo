package org.qiunet.flash.handler.context.response.push;

import org.qiunet.utils.math.BitUtil;

/***
 *
 * @author qiunet
 * 2023/3/29 15:41
 */
public interface IExtraInfo {
	enum ExtraInfoType {
		SERVER_NODE_MSG,
		CROSS_PLAYER_MSG,
		FLUSH,
		KCP,
		;
		/**
		 * 是否在标记位为true
		 * @param extraInfo 原始值
		 * @return true 表示有
		 */
		public boolean gotTruth(int extraInfo) {
			return BitUtil.isBitSet(extraInfo, this.ordinal());
		}

		/**
		 * 设定值
		 * @param ori 原始值
		 * @return 设定后的值
		 */
		public int setVal(int ori) {
			return BitUtil.setBit(ori, this.ordinal());
		}
	}
	/**
	 * 作为Kcp消息
	 * @return
	 */
	IChannelMessage<?> asCrossPlayerMsg();

	/**
	 * 获得ExtraInfo
	 * @return
	 */
	int getExtraInfo();
	/**
	 * 作为ServerNode message
	 * @return
	 */
	IChannelMessage<?> asServerNodeMsg();
	/**
	 * 作为Kcp消息
	 * @return
	 */
	IChannelMessage<?> asKcpMsg();

	/**
	 * 默认需要Flush
	 * @return
	 */
	default IChannelMessage<?> needFlush() {
		return needFlush(true);
	}

	IChannelMessage<?> needFlush(boolean flush);

	/**
	 * 回收
	 */
	void recycle();

	/**
	 * 添加 key val
	 * @param key key
	 * @param val val
	 */
	IChannelMessage<?> add(String key, Object val);

	/**
	 * 是否包含某个key
	 * @param key key
	 * @return true 包含
	 */
	default boolean containKey(String key) {
		return get(key) != null;
	}

	/**
	 * 获得对象
	 * @param key
	 * @return
	 */
	Object get(String key);
}
