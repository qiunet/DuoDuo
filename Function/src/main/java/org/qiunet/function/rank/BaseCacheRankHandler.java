package org.qiunet.function.rank;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.CommMessageHandler;

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
	private Map<Long, RankVo> rankMap = Maps.newHashMap();
	private TreeSet<RankVo> rankVos;

	protected BaseCacheRankHandler() {
		Preconditions.checkState(getType().rankSize() > 0 && getType().rankSize() < 1000);
		this.rankVos = new TreeSet<>(getType().comparator());

		List<RankVo> loadData = this.load();
		for (RankVo rankVo : loadData) {
			rankMap.put(rankVo.getId(), rankVo);
		}
		rankVos.addAll(loadData);
	}

	@Override
	public void updateRank(long id, String name, long value, Map<String, String> extraInfo) {
		if (! getType().canRank(id, value)) {
			if (rankMap.containsKey(id)) {
				this.removeRank(id);
			}
			return;
		}

		messageHandler.addMessage(h -> {
			this.alterRankValue0(id, name, value, extraInfo);
		});
	}

	private void alterRankValue0(long id, String name, long value, Map<String, String> extraInfo) {
		RankVo rankVo = rankMap.get(id);
		if (rankVo == null) {
			boolean rankFull;
			if ((rankFull = rankVos.size() >= getType().rankSize())
				&& value <= rankVos.last().getValue()) {
				return;
			}

			rankVo = RankVo.valueOf(id, name, value, extraInfo);
			if (rankFull) {
				RankVo last = rankVos.pollLast();
				rankMap.remove(last.getId());
			}
			rankMap.put(id, rankVo);
		}else {
			rankVos.remove(rankVo);
			rankVo.updateValue(value);
		}
		rankVos.add(rankVo);
		this.resort();
		messageHandler.addMessage(h -> {
			this.save(Lists.newArrayList(rankVos));
		});
	}

	/**
	 * 重新排序
	 */
	private void resort(){
		messageHandler.addMessage(h -> {
			int rank = 1;
			for (RankVo vo : rankVos) {
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
	private RankVo removeRank0(long id) {
		RankVo rankVo = rankMap.remove(id);
		if (rankVo != null) rankVos.remove(rankVo);
		return rankVo;
	}


	@Override
	public RankVo getRankVo(long id) {
		return rankMap.get(id);
	}


	@Override
	public List<RankVo> getRankVos(int startRank, int size) {
		return rankVos.stream()
			.filter(vo -> vo.rank >= startRank)
			.limit(size).collect(Collectors.toList());
	}

	@Override
	public void clear() {
		this.rankVos.clear();
		this.rankMap.clear();
	}

	/**
	 * 保存数据
	 */
	protected abstract void save(List<RankVo> rankVoList);

	/**
	 * 加载数据
	 * @return
	 */
	protected abstract List<RankVo> load();
}
