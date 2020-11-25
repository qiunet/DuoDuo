package org.qiunet.function.rank;

import com.google.common.collect.ComparisonChain;

import java.util.Comparator;

/***
 * 排行榜枚举接口
 *
 * @author qiunet
 * 2020-11-24 09:48
 */
public interface IRankType {
	/**
	 * type的int值
	 * @return
	 */
	int type();
	/**
	 * 枚举名
	 * @return
	 */
	String name();

	/**
	 * 描述
	 * @return
	 */
	String desc();

	/**
	 * 排序
	 * value数值大的排前, dt越早越前
	 *
	 * @return
	 */
	default Comparator<RankVo> comparator() {
		return (o1, o2) -> ComparisonChain.start()
			.compare(o2.getValue(), o1.getValue())
			.compare(o1.getDt(), o2.getDt())
			.result();
	}

	/**
	 * 排名大小
	 * 不建议太大.
	 * @return
	 */
	default int rankSize(){
		return 100;
	}

	/**
	 * 是否可以进入排名.
	 * 有时候, 要求value > 多少才进入排行 .
	 * @param id 排名的id
	 * @param value 排名值
	 * @return
	 */
	default boolean canRank(long id, long value) {
		return true;
	}
}
