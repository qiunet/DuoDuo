package org.qiunet.test.function.test.cd;

import org.qiunet.function.cd.ICdType;

import java.util.concurrent.TimeUnit;

/***
 * 业务的各种cd类型
 *
 * @author qiunet
 * 2020-03-02 11:57
 ***/
public enum CdType implements ICdType {
	CHAT(20000, "聊天cd"),
	FIGHT(60000, "战斗cd"),
	GUILD_JOIN(24, TimeUnit.HOURS, 2, "公会加入cd"),
	;

	/** 间隔时间*/
	private final long period;
	/**时间单位**/
	private final TimeUnit unit;
	/**描述*/
	private final String desc;
	/**cd内次数*/
	private final int limitCount;

	CdType(long period, String desc) {
		this(period, TimeUnit.MILLISECONDS, desc);
	}

	CdType(long period, TimeUnit unit, String desc) {
		this(period, unit, 1, desc);
	}
	CdType(long period, TimeUnit unit, int limitCount, String desc) {
		this.limitCount = limitCount;
		this.period = period;
		this.unit = unit;
		this.desc = desc;
	}

	@Override
	public int limitCount() {
		return limitCount;
	}

	@Override
	public long period() {
		return period;
	}

	@Override
	public TimeUnit unit() {
		return unit;
	}

	@Override
	public String desc() {
		return desc;
	}
}
