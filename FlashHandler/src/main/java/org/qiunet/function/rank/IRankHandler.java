package org.qiunet.function.rank;

import java.util.List;

/***
 * 排行 处理器
 *
 * @author qiunet
 * 2020-11-24 09:50
 */
public interface IRankHandler<Type extends Enum<Type> & IRankType> {

	/**
	 * 排行榜的类型
	 * @return
	 */
	Type getType();

	/**
	 * 修改排行值
	 */
	void updateRank(RankData rankData);
	/**
	 * 获得排行数据
	 * @param id 排行id
	 * @return
	 */
	RankData getRankVo(long id);
	/**
	 * 获得指定长度排行榜列表
	 * @param size 长度
	 * @return
	 */
	default List<RankData> getRankVos(int size){
		return getRankVos(1, size);
	}

	/***
	 * 获得指定区间的排行榜列表
	 * @param startRank 起始排名
	 * @param size 长度
	 * @return
	 */
	List<RankData> getRankVos(int startRank, int size);

	/**
	 * 清理排行
	 */
	void clear();
}
