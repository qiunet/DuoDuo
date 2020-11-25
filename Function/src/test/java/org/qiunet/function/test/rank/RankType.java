package org.qiunet.function.test.rank;

import org.qiunet.function.rank.IRankType;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 11:28
 */
public enum RankType implements IRankType {
	LEVEL(1, "等级排行"){
		@Override
		public boolean canRank(long id, long value) {
			return value > 5;
		}
	},
	;
	private int type;
	private String desc;

	RankType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Override
	public int type() {
		return type;
	}

	@Override
	public String desc() {
		return desc;
	}
}
