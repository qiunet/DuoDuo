package org.qiunet.function.rank;

import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.exceptions.CustomException;

/***
 * 构造一个RankData
 *
 * @author qiunet
 * 2021/12/17 15:32
 */
public class RankDataBuilder {

	private final RankData rankData;

	RankDataBuilder(long id, long value) {
		this.rankData = RankData.valueOf(id, value);
	}

	/**
	 * 添加名字
	 * @param name
	 * @return
	 */
	public RankDataBuilder addName(String name) {
		return addData(RankData.NAME, name);
	}
	/**
	 * 添加一些字段.
	 * @param key
	 * @param val
	 * @return
	 */
	public RankDataBuilder addData(String key, Object val) {
		if (CommonUtil.existInList(key, "_id", "_value", "_dt")) {
			throw new CustomException("{} is key word!", key);
		}
		this.rankData.put(key, val);
		return this;
	}

	public RankData build(){
		return rankData;
	}
}
