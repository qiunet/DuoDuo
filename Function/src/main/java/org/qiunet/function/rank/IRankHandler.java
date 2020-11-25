package org.qiunet.function.rank;

import java.util.List;
import java.util.Map;

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
	 * @param id 排行的key
	 * @param value 修改值
	 * @param extraInfo 额外信息
	 */
	void updateRank(long id, String name, long value, Map<String, String> extraInfo);

	/**
	 * 修改排行值
	 * @param id
	 * @param value
	 */
	default void updateRank(long id, String name, long value) {
		this.updateRank(id, name, value, null);
	}

	/**
	 * 获得排行数据
	 * @param id 排行id
	 * @return
	 */
	RankVo getRankVo(long id);
	/**
	 * 获得指定长度排行榜列表
	 * @param size 长度
	 * @return
	 */
	default List<RankVo> getRankVos(int size){
		return getRankVos(1, size);
	}

	/***
	 * 获得指定区间的排行榜列表
	 * @param startRank 起始排名
	 * @param size 长度
	 * @return
	 */
	List<RankVo> getRankVos(int startRank, int size);

	/**
	 * 清理排行
	 */
	void clear();
}
