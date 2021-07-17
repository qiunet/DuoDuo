package org.qiunet.utils.test.protobuf;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 *
 *
 * @author qiunet
 * 2020-10-16 11:53
 */
@ProtobufClass
public class BasePbData {

	private long playerId;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
