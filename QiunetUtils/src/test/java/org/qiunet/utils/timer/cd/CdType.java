package org.qiunet.utils.timer.cd;

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
	GUILD_JOIN(24, TimeUnit.HOURS, "公会加入cd"),
	;

	/** 间隔时间*/
	private long period;
	/**时间单位**/
	private TimeUnit unit;
	/**描述*/
	private String desc;

	CdType(long period, String desc) {
		this(period, TimeUnit.MILLISECONDS, desc);
	}

	CdType(long period, TimeUnit unit, String desc) {
		this.period = period;
		this.unit = unit;
		this.desc = desc;
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
