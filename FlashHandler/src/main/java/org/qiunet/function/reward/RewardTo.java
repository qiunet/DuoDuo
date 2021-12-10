package org.qiunet.function.reward;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/***
 *
 * @author qiunet
 * 2021/12/10 13:53
 */
@ProtobufClass(description = "奖励传输类")
public class RewardTo {
	@Protobuf(description = "资源ID")
	private int cfgId;
	@Protobuf(description = "数量")
	private long value;

	public static RewardTo valueOf(int cfgId, long value) {
		RewardTo data = new RewardTo();
		data.cfgId = cfgId;
		data.value = value;
		return data;
	}

	public int getCfgId() {
		return cfgId;
	}

	public void setCfgId(int cfgId) {
		this.cfgId = cfgId;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}
