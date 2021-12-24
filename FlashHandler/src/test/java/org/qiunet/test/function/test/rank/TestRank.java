package org.qiunet.test.function.test.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.function.rank.RankData;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 *
 * @author qiunet
 * 2020-11-25 12:55
 */
public class TestRank {
	@BeforeAll
	public static void init(){
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner();
	}

	@Test
	public void testCacheRank() throws InterruptedException {
		AtomicInteger max = new AtomicInteger();
		for (int i = 0; i < 100; i++) {
			int random = MathUtil.random(2000);
			if (random > max.intValue()) {
				max.set(random);
			}
			LevelCacheRank.instance.updateRank(RankData.custom(i, random).addName("测试"+i).build());
		}

		Thread.sleep(500);
		List<RankData> rankDatas = LevelCacheRank.instance.getRankVos(1000);
		Assertions.assertEquals(RankType.LEVEL.rankSize(), rankDatas.size());
		Assertions.assertEquals(max.intValue(), rankDatas.get(0).getValue());

		RankData rankData = LevelCacheRank.instance.getRankVo(rankDatas.get(0).getId());
		Assertions.assertEquals(1, rankData.gotRank());
	}


	@Test
	public void testRedisRank() throws InterruptedException {
		LevelRedisRank.instance.clear();

		AtomicInteger max = new AtomicInteger();
		for (int i = 0; i < 100; i++) {
			int random = MathUtil.random(2000);
			if (random > max.intValue()) {
				max.set(random);
			}
			LevelRedisRank.instance.updateRank(RankData.custom(i, random).addName("测试"+i).build());
		}

		Thread.sleep(500);
		List<RankData> rankDatas = LevelRedisRank.instance.getRankVos(1000);
		Assertions.assertEquals(RankType.LEVEL.rankSize(), rankDatas.size());
		Assertions.assertEquals(max.intValue(), rankDatas.get(0).getValue());

		RankData rankData = LevelRedisRank.instance.getRankVo(rankDatas.get(0).getId());
		Assertions.assertEquals(1, rankData.gotRank());
	}
}
