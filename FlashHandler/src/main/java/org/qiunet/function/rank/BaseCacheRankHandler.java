package org.qiunet.function.rank;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.CommMessageHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/***
 * 本服排行
 *
 * @author qiunet
 * 2020-11-24 11:17
 */
public abstract class BaseCacheRankHandler<Type extends Enum<Type> & IRankType>
	implements IRankHandler<Type> {
	private static final CommMessageHandler messageHandler = new CommMessageHandler();
	private final Map<Long, RankData> rankMap = Maps.newHashMap();
	private final TreeSet<RankData> rankData;

	protected BaseCacheRankHandler() {
		Preconditions.checkState(getType().rankSize() > 0 && getType().rankSize() < 1000);
		this.rankData = new TreeSet<>(getType().comparator());

		List<RankData> loadData = this.load();
		for (RankData rankData : loadData) {
			rankMap.put(rankData.getId(), rankData);
		}
		rankData.addAll(loadData);
		this.resort();
	}

	@Override
	public void updateRank(RankData rankData) {
		if (! getType().canRank(rankData.getId(), rankData.getValue())) {
			if (rankMap.containsKey(rankData.getId())) {
				this.removeRank(rankData.getId());
			}
			return;
		}

		messageHandler.addMessage(h -> {
			this.alterRankValue0(rankData);
		});
	}

	private void alterRankValue0(RankData rankData) {
		RankData currData = rankMap.get(rankData.getId());
		if (currData == null) {
			boolean rankFull;
			if ((rankFull = this.rankData.size() >= getType().rankSize())
				&& rankData.getValue() <= this.rankData.last().getValue()) {
				return;
			}

			if (rankFull) {
				RankData last = this.rankData.pollLast();
				if (last != null) {
					RankData remove = rankMap.remove(last.getId());
					this.rankData.remove(remove);
				}
			}
			rankMap.put(rankData.getId(), rankData);
		}else {
			this.rankData.remove(currData);
		}
		this.rankData.add(rankData);
		this.resort();
		messageHandler.addMessage(h -> {
			this.save(this.rankData);
		});
	}

	/**
	 * 重新排序
	 */
	private void resort(){
		messageHandler.addMessage(h -> {
			int rank = 1;
			for (RankData vo : rankData) {
				vo.rank = rank++;
			}
		});
	}

	/**
	 * 移除
	 * @param id 排行id
	 */
	private void removeRank(long id) {
		messageHandler.addMessage(h -> this.removeRank0(id));
	}

	/**
	 * 删除. 并返回rankVo
	 * @param id
	 * @return
	 */
	private RankData removeRank0(long id) {
		RankData rankData = rankMap.remove(id);
		if (rankData != null) this.rankData.remove(rankData);
		return rankData;
	}


	@Override
	public RankData getRankVo(long id) {
		return rankMap.get(id);
	}


	@Override
	public List<RankData> getRankVos(int startRank, int size) {
		return rankData.stream()
			.filter(vo -> vo.rank >= startRank)
			.limit(size).collect(Collectors.toList());
	}

	@Override
	public void clear() {
		this.rankData.clear();
		this.rankMap.clear();
	}

	/**
	 * 保存数据
	 */
	protected abstract void save(Collection<RankData> rankDataList);

	/**
	 * 加载数据
	 * @return
	 */
	protected abstract List<RankData> load();
}
